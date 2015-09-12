package helpers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
 
public class FileDownloader {
 
    WebDriver driver;
 
    public FileDownloader(WebDriver driver) {
        this.driver = driver;
    }
 
    /** Download the file specified in the href attribute of a WebElement
     * @param element - page element (file) to download
     * @param fileName - path to place where we want a file/image to be downloaded
     * @return
     * @throws Exception
     */
    public void downloadFile(WebElement element, String fileName) throws Exception {
        download(element, "href", fileName);
    }
 
    /** Download the image specified in the src attribute of a WebElement
     * @param element - page element (image) to download
     * @param fileName - path to place where we want a file/image to be downloaded
     * @return
     * @throws Exception
     */
    public void downloadImage(WebElement element, String fileName) throws Exception {
        download(element, "src", fileName);
    }
 
    /** Perform the file/image download
     * @param element - page element to work with
     * @param attribute - attribute of page element which contains the path to file/image location (e.g. src of image)
     * @param fileName - path to place where we want a file/image to be downloaded  
     * @return
     * @throws IOException
     */
    private void download(WebElement element, String attribute, String fileName) throws IOException {
        
        String fileToDownloadLocation = element.getAttribute(attribute);
        HttpGet httpget = new HttpGet(fileToDownloadLocation);

		HttpClient client = new DefaultHttpClient();
        File downloadedFile = new File(fileName);
               
        HttpResponse response = client.execute(httpget);
        FileUtils.copyInputStreamToFile(response.getEntity().getContent(), downloadedFile);
        response.getEntity().getContent().close();
    } 
}
