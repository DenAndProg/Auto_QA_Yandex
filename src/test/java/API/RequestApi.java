package API;

import Methods.*;
import org.junit.Test;


import static MyData.EndPointsApi.urlFile;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

public class RequestApi {

    public static String nameFolder = "MyFile";
    public static String nameFile = "Test.txt";
    public static String dirToFile = nameFolder + "/" + nameFile;
    public static String dirToFile2 = nameFolder + "/" + "file.txt";

    @Test
    public void firstTest() throws InterruptedException {

        //создаем папку
        Methods.createFolder(nameFolder)
                .assertThat().statusCode(201);

        Methods.checkFile(nameFolder);

        Methods.deleteFile(nameFolder);

        Methods.checkFile(nameFolder);
    }

    @Test
    public void secondTest() throws InterruptedException {
        Methods.createFolder(nameFolder);

        Methods.createFile(dirToFile, urlFile);

        Methods.deleteFile(dirToFile);

        Methods.deleteFile(nameFolder);
    }

   @Test
    public void thirtyTest() throws InterruptedException {

        Methods.createFolder(nameFolder);

        Methods.createFile(dirToFile, urlFile);

        Methods.deleteFile(dirToFile);

        Methods.restoreTrash(Methods.checkDeleteFile(nameFile));
    }

    @Test
    public void fourthTest() throws InterruptedException {

        Methods.createFolder(nameFolder);

        Methods.createFile(dirToFile, urlFile);

        Methods.createFile(dirToFile2, urlFile);

        Integer beforeSizeDustbin = Methods.checkDeleteFile();

        Methods.deleteFile(dirToFile);

        Methods.deleteFile(dirToFile2);

        Integer s4 = Methods.checkDelete("Test.txt", "file.txt");

        Integer sizeDustbin = Methods.checkDeleteFile();

        assert(sizeDustbin == s4 + beforeSizeDustbin);

        Methods.restoreTrash(Methods.checkDeleteFile(nameFile));

        Methods.restoreTrash(Methods.checkDeleteFile("file.txt"));

        Methods.deleteFile(nameFolder);
    }

    @Test
    public void fiftiethTest() throws InterruptedException {

        Methods.createFolder("test");

        Methods.createFolder("test/foo");

        Methods.createFile("test/foo/autotest.txt", urlFile);

        Methods.checkFile2("test")
                .statusCode(200)
                .body("_embedded.items.name", everyItem((equalTo("foo"))))
                .body("_embedded.items.path", everyItem((equalTo("disk:/test/foo"))));

        Methods.deleteFile("test");

        Methods.checkDelete("test")
                .statusCode(200)
                .body("_embedded.items.name", everyItem((equalTo("test"))));
    }

    @Test
    public void sixtiethTest() throws InterruptedException {
        Methods.createFolder("test");

        Methods.createFolder("test/foo");

        Methods.createFile("test/foo/autotest.txt", urlFile);

        Methods.deleteFile("test");

        Methods.deleteAll();

        Methods.checkDelete("test")
                .statusCode(200)
                .body("_embedded.items", everyItem((equalTo(0))));
    }
}
