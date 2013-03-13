package com.vtaccess;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.vtaccess.exceptions.WrongLoginException;

/**
 * Used to connect to the Virginia
 * Tech CAS authentication system via SSL. Secure. Many services in this API take/contain
 * a Cas session, and it must be active for them to work. 
 * 
 * @author Ethan Gaebel (egaebel)
 *
 */
public final class Cas {

    //~Constants-----------------------------------------------
    /**
     * the users agents to pass along with the response
     */
    private static final String AGENTS = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) Gecko/20100101 Firefox/11.0";
    /**
     * The url for hokiespa to login at.
     */
    private static final String LOGIN = "https://auth.vt.edu/login?service=https://webapps.banner.vt.edu/banner-cas-prod/authorized/banner/SelfService";
    /**
     * The URL to logout of CAS entirely.
     */
    private static final String CAS_LOGOUT = "https://auth.vt.edu/logout";
    /**
     * String to check for recovery options screen with.
     */
    private static final String RECOVERY_OPTIONS_STRING = "You have not updated account recovery options in the past";
    
    //~Data Fields---------------------------------------------
    /**
     * The cookies that are used with HokieSpa.
     */
    private Map<String, String> cookies;
    /**
     * The SSL certificate used to connect to CAS.
     */
    private File cert;
    /**
     * The path on which to save the SSL certificate.
     * May be null, causes default path to be used.
     */
    private String certFilePath;
    /**
     * Boolean value denoting whether this CAS object has an active
     * connection to the VT CAS system. True if its active, false otherwise.
     */
    private boolean active;
    /**
     * The stored username of the user of this session.
     */
    private char[] username;
    /**
     * The stored password of the user of this session.
     */
    private char[] password;
    /**
     * Indicates if the username and password values stored in this Cas object are valid
     * for initiating a Cas session.
     * 
     * Note: it is false when username and password are empty.
     */
    private boolean validLoginInfo;
    /**
     * Boolean value indicating if the Cas session is going to need refreshing
     */
    private boolean refreshSession;

    //~Constructors--------------------------------------------
    
    /**
     * Constructor taking a username and password to be used with HokieSpa
     * session management. 
     * Logs into CAS immediately.
     * 
     * @param username a character array that is a HokieSpa username.
     * @param password a character array that is a HokieSpa password.
     * 
     * @throws WrongLoginException indicates that the username or password was incorrect.
     */
    public Cas(char[] username, char[]  password) throws WrongLoginException {
        
        //initialize all booleans to be pessimistic
            //they are set to wokring values if login works
        active = false;
        validLoginInfo = false;
        refreshSession = true;
        
        this.username = new char[username.length];
        for (int i = 0; i < username.length; i++) {
            this.username[i] = username[i];
        }
        
        this.password = new char[password.length];
        for (int i = 0; i < password.length; i++) {
            this.password[i] = password[i];
        }
        
        
        //clear out passed username and password
        for (int i = 0; i < username.length; i++) {
            
            username[i] = 0;
        }
        for (int i = 0; i < password.length; i++) {
            
            password[i] = 0;
        }
        
        certFilePath = null;
        
        try {
            validLoginInfo = login(this.username, this.password);
        }
        catch (WrongLoginException e) {
        
            clearUserData();
            throw new WrongLoginException();
        }
    }
    
    /**
     * Constructor taking a username and password to be used with HokieSpa
     * session management; and a filePath to save the SSL certificate to.
     * Logs into CAS immediately.
     * 
     * @param username a character array that is a HokieSpa username.
     * @param password a character array that is a HokieSpa password.
     * @param filePath the path to the file to save the SSL certificate in.
     * 
     * @throws WrongLoginException indicates that the username or password was incorrect. 
     */
    public Cas(char[] username, char[] password, String filePath) throws WrongLoginException {
        
        //initialize all booleans to be pessimistic
            //they are set to wokring values if login works
        active = false;
        validLoginInfo = false;
        refreshSession = true;
        
        this.username = new char[username.length];
        for (int i = 0; i < username.length; i++) {
            this.username[i] = username[i];
        }
        
        this.password = new char[password.length];
        for (int i = 0; i < password.length; i++) {
            this.password[i] = password[i];
        }
        
        //clear passed in username and password
        for (int i = 0; i < username.length; i++) {
            
            username[i] = 0;
        }
        for (int i = 0; i < password.length; i++) {
            
            password[i] = 0;
        }
        
        certFilePath = filePath;
        validLoginInfo = false;
        refreshSession = false;
        
        try {
            validLoginInfo = login(this.username, this.password, filePath);
        }
        catch (WrongLoginException e) {
        
            clearUserData();
            throw new WrongLoginException();
        }
    }

    //~Methods-------------------------------------------------
    /**
     * Refreshes a Cas session that has timed out.
     * 
     * @return true if the session was refreshed, false if it was not.
     *          Note: a session may fail to be refreshed if the username/password are incorrect.
     */
    public boolean refreshSession() {

        try {
            
            boolean returnVal = false;
            
            if (certFilePath == null) {
                logout();
                returnVal = login(username, password);
            }
            else {
                logout();
                returnVal = login(username, password, certFilePath);
            }
            
            return returnVal;
        }
        catch (WrongLoginException e) {}
        
        return false;
    }
    
    /**
     * Switches the user that is active on this session to the passed in username and password.
     * 
     * @param username the new username to use.
     * @param password the new password to use.
     * 
     * @throws WrongLoginException indicates that the username or password was incorrect.
     */
    public void switchUsers(char[] username, char[] password) throws WrongLoginException {

        logout();
        clearUserData();
        this.username = new char[username.length];
        for (int i = 0; i < username.length; i++) {
            this.username[i] = username[i];
        }
        
        this.password = new char[password.length];
        for (int i = 0; i < password.length; i++) {
            this.password[i] = password[i];
        }
        
        //clear out passed username and password
        for (int i = 0; i < username.length; i++) {
            
            username[i] = 0;
        }
        for (int i = 0; i < password.length; i++) {
            
            password[i] = 0;
        }
        
        try {
            validLoginInfo = login(this.username, this.password);
        }
        catch (WrongLoginException e) {
        
            clearUserData();
            throw new WrongLoginException();
        }
    }
    
    /**
     * Switches the user that is active on this session to the passed in username and password.
     * Logs in with new user immediately.
     * 
     * @param username the new username to use.
     * @param password the new password to use.
     * @param filePath a new filePath to use for the SSL certificate.
     * 
     * @throws WrongLoginException indicates that the username or password was incorrect.
     */
    public void switchUsers(char[] username, char[] password, String filePath) throws WrongLoginException {

        closeSession();
        this.username = new char[username.length];
        for (int i = 0; i < username.length; i++) {
            this.username[i] = username[i];
        }
        
        this.password = new char[password.length];
        for (int i = 0; i < password.length; i++) {
            this.password[i] = password[i];
        }
        
        
        //Clear out passed username and password
        for (int i = 0; i < username.length; i++) {
            
            username[i] = 0;
        }
        for (int i = 0; i < password.length; i++) {
            
            password[i] = 0;
        }
        
        try {
            validLoginInfo = login(this.username, this.password, certFilePath);
        }
        catch (WrongLoginException e) {
        
            clearUserData();
            throw new WrongLoginException();
        }
    }
    
    
    /**
     * Closes down this Cas session.
     * 
     * @return true if the session closed successfully, false if it did not close.
     */
    public boolean closeSession() {

        clearUserData();
        return logout();
    }
    
    /**
     * Grabs the needed SSL Certificate from the CAS login page.
     * 
     * @return true if successful, false if there was an IOException, 
     *          MalformedURLException, SSLPeerUnverifiedException, 
     *          or a CertificateEncodingException.
     *          See StackTrace to figure out which one.
     */
    private boolean grabCertificate() {
        
        try {
            URL url = new URL(LOGIN);
            HttpsURLConnection connect = (HttpsURLConnection)url.openConnection();
            connect.connect();
            Certificate[] certs = connect.getServerCertificates();
            
            if (certs.length > 0) {
             
                cert = new File("Resources/auth.vt.edu.jks");
                //write the certificate obtained to the cert file.
                OutputStream os = new FileOutputStream(cert);
                os.write(certs[0].getEncoded());
                os.close();
                
                return true;
            }
        }
        catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Grabs the needed SSL Certificate from the CAS login page. Saves the file to the passed in
     * directory.
     * 
     * @return true if successful, false if there was an IOException, 
     *          MalformedURLException, SSLPeerUnverifiedException, 
     *          or a CertificateEncodingException.
     *          See StackTrace to figure out which one.
     */
    private boolean grabCertificate(String filePath) {
        
        try {
            URL url = new URL(LOGIN);
            HttpsURLConnection connect = (HttpsURLConnection)url.openConnection();
            connect.connect();
            Certificate[] certs = connect.getServerCertificates();
            
            if (certs.length > 0) {
             
                cert = new File(filePath + "auth.vt.edu.jks");
                //write the certificate obtained to the cert file.
                OutputStream os = new FileOutputStream(cert);
                os.write(certs[0].getEncoded());
                os.close();
                
                return true;
            }
        }
        catch (SSLPeerUnverifiedException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (CertificateEncodingException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Takes in a username and password and logs into CAS
     * with the supplied username and password.
     * 
     * For best performance this method call should be done in a separate thread.
     * 
     * @param username the username of the user.
     * @param password the password of the user.
     * @return returns true if successful, false if there was an IOException, 
     *          and false if the Certificate wasn't correctly obtained. 
     * 
     * @throws WrongLoginException indicates that the username or password was incorrect.
     */
    private boolean login(char[] username, char[] password) throws WrongLoginException {

        try {

            if(grabCertificate()) {

                System.setProperty("javax.net.ssl.trustStore", cert.getAbsolutePath());
    
                // get three hidden fields, and cookies from initial Login Page
                Response loginPageResp = Jsoup.connect(LOGIN).execute();
    
                // save JSESSION cookie from the LOGIN URL's response
                cookies = loginPageResp.cookies();
    
                // get the document from the response to retrieve hidden fields
                Document doc = loginPageResp.parse();
    
                // select the correct div section under form-->fieldset
                // Element form = doc.select("form").first();
                Elements divs = doc.select("form fieldset div");
                Element div6 = divs.get(5);
    
                // hashmap to hold hiddenFields in document, as well as username,
                // password
                Map<String, String> hiddenFields = new HashMap<String, String>();
    
                // place hidden fields & _submit into hashmap for passing
                hiddenFields.put("lt", div6.getElementsByIndexEquals(0).val());
                hiddenFields.put("execution", div6.getElementsByIndexEquals(1)
                        .val());
                hiddenFields
                        .put("_eventId", div6.getElementsByIndexEquals(2).val());
    
                // will always be this value on the CAS page
                hiddenFields.put("submit", "_submit");
    
                // place username and password into hashmap for passing
                hiddenFields.put("username", String.copyValueOf(username));
                hiddenFields.put("password", String.copyValueOf(password));
    
                // enter in the hidden fields as well as username and pasword --
                // press submit, USE GET METHOD!!!
                Response resp = Jsoup
                        .connect(LOGIN)
                        .data(hiddenFields)
                        .cookie("JSESSIONID", cookies.get("JSESSIONID"))
                        .method(Method.GET)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .referrer(LOGIN).userAgent(AGENTS).execute();
    
                // get all cookies from the resp generated above to use in future
                // authentication
                cookies.putAll(resp.cookies());
    
                Document loginCheck = resp.parse();
    
                // check to see if the login was successful
                Elements checkLoginEls = loginCheck.select("#login-error");
                Elements checkRecoveryEls = loginCheck.select("#warn");
                
                //check for correct username/password
                if (checkLoginEls.size() > 0) {
    
                    throw new WrongLoginException();
                }
                //check for recovery options warning
                else if (checkRecoveryEls.size() > 0 && checkRecoveryEls.get(0).text().trim().contains(RECOVERY_OPTIONS_STRING)) {
                    
                    resp = Jsoup.connect(resp.url().toString())
                            .cookies(hiddenFields)
                            .cookie("JSESSIONID", cookies.get("JSESSIONID"))
                            .method(Method.GET)
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .userAgent(AGENTS).execute();
                    
                    cookies.putAll(resp.cookies());
                }
    
                active = true;
                refreshSession = false;

                return true;
            }
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * Takes in a username and password and logs into CAS
     * with the supplied username and password. Also takes in a String that specifies
     * the directory where the SSL should be saved. This is especially useful in Android
     * usage, because the file system differs from java's default one.
     * 
     * For best performance this method call should be done in a separate thread.
     * 
     * @param username the username of the user.
     * @param password the password of the user.
     * @param filePath the file path that the SSL certificate should be saved in.
     * @return returns true if successful, false if there was an IOException, 
     *          and false if the Certificate wasn't correctly obtained. 
     * 
     * @throws WrongLoginException indicates that the username or password was incorrect
     */
    private boolean login(char[] username, char[] password, String filePath) throws WrongLoginException {

        try {

            if(grabCertificate(filePath)) {
            
                System.setProperty("javax.net.ssl.trustStore", cert.getAbsolutePath());
    
                // get three hidden fields, and cookies from initial Login Page
                Response loginPageResp = Jsoup.connect(LOGIN).execute();
    
                // save JSESSION cookie from the LOGIN URL's response
                cookies = loginPageResp.cookies();
    
                // get the document from the response to retrieve hidden fields
                Document doc = loginPageResp.parse();
    
                // select the correct div section under form-->fieldset
                // Element form = doc.select("form").first();
                Elements divs = doc.select("form fieldset div");
                Element div6 = divs.get(5);
    
                // hashmap to hold hiddenFields in document, as well as username,
                // password
                Map<String, String> hiddenFields = new HashMap<String, String>();
    
                // place hidden fields & _submit into hashmap for passing
                hiddenFields.put("lt", div6.getElementsByIndexEquals(0).val());
                hiddenFields.put("execution", div6.getElementsByIndexEquals(1)
                        .val());
                hiddenFields
                        .put("_eventId", div6.getElementsByIndexEquals(2).val());
    
                // will always be this value on the CAS page
                hiddenFields.put("submit", "_submit");
    
                // place username and password into hashmap for passing
                hiddenFields.put("username", String.copyValueOf(username));
                hiddenFields.put("password", String.copyValueOf(password));
    
                // enter in the hidden fields as well as username and pasword --
                // press submit, USE GET METHOD!!!
                Response resp = Jsoup
                        .connect(LOGIN)
                        .data(hiddenFields)
                        .cookie("JSESSIONID", cookies.get("JSESSIONID"))
                        .method(Method.GET)
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .referrer(LOGIN).userAgent(AGENTS).execute();
    
                // get all cookies from the resp generated above to use in future
                // authentication
                cookies.putAll(resp.cookies());
    
                Document loginCheck = resp.parse();
    
                // check to see if the login was successful, that is, had a correct PASSWORD and USERNAME
                Elements checkEls = loginCheck.select("#login-error");
                Elements checkRecovery = loginCheck.select("#warn");
                
                if (checkEls.size() > 0) {

                    // if unsuccessful login throw WrongLoginException
                    throw new WrongLoginException();
                }
                //Check for account recovery options page
                else if (checkRecovery != null && checkRecovery.text().contains(RECOVERY_OPTIONS_STRING)) {
                                        
                    resp = Jsoup.connect(resp.url().toString())
                            .cookies(hiddenFields)
                            .cookie("JSESSIONID", cookies.get("JSESSIONID"))
                            .method(Method.GET)
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .userAgent(AGENTS).execute();
                    
                    cookies.putAll(resp.cookies());
                }
    
                active = true;
                refreshSession = false;
                
                return true;
            }
        }
        catch (SocketTimeoutException e) {
            e.printStackTrace();
            throw new WrongLoginException();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * End this CAS session. If this returns false the session stays open.
     * 
     * @return true if successful, false if an error occurred (the session will remain open).
     */
    private boolean logout() {

        boolean val;
        
        try {
            // logs out of CAS. closing the session
            Jsoup.connect(CAS_LOGOUT).get();

            val = true;
        }
        catch (IOException e) {
            e.printStackTrace();
            val = false;
        }
        
        active = false;
        cookies = null;
        
        return val;
    }
    
    /**
     * Erases user data stored in this class.
     */
    private void clearUserData() {
        
        for (int i = 0; i < username.length; i++) {
            username[i] = 0;
        }
        for (int i = 0; i < password.length; i++) {
            password[i] = 0;
        } 
    }
    
    //~Getters and Setters--------------------------------------------------------------
    /**
     * Getter for cookies.
     * 
     * @return cookies the cookies that have been pulled from Cas, if a login has occurred. Otherwise, returns null
     */
    protected Map<String, String> getCookies() {
        
        refreshSession = true;
        return cookies;
    }

    /**
     * Tests to see if this CAS object has an active
     * login session with hokiespa.
     * 
     * @return true if active, false otherwise.
     */
    public boolean isActive() {

        if (refreshSession) {
            if (!refreshSession()) {
                active = false;
            }
        }
        return active;
    }
    
    /**
     * Indicates whether the login information stored in this Cas object is valid.
     * Validity is determined when attempting to log into hokiespa.
     * 
     * @return true if login information is valid, false otherwise.
     */
    public boolean isValidLoginInfo() {
        
        return validLoginInfo;
    }

    /**
     * @return the refreshSession
     */
    public boolean isRefreshSession() {

        return refreshSession;
    }

    /**
     * @param refreshSession the refreshSession to set
     */
    public void setRefreshSession(boolean refreshSession) {

        this.refreshSession = refreshSession;
    }
}
