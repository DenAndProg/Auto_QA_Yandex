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

        //Создаем папку
        Methods.createFolder(nameFolder)
                .statusCode(201);

        //Проверяем что папка существует
        Methods.checkFile(nameFolder)
                .statusCode(200)
                .body("name", (equalTo(nameFolder)));

        //Удаляем папку
        Methods.deleteFile(nameFolder)
                .statusCode(204);

        //Проверяем что папка удалена
        Methods.checkFile(nameFolder)
                .statusCode(404);

    }

    @Test
    public void secondTest() throws InterruptedException {

        //Создаем папку
        Methods.createFolder(nameFolder)
                .statusCode(201);

        //Создаем файл
        Methods.createFile(dirToFile, urlFile)
                .statusCode(202);

        //Удаляем файл
        Methods.deleteFile(dirToFile)
                .statusCode(204);

        //Удаляем папку
        Methods.deleteFile(nameFolder)
                .statusCode(204);

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

        //Очищаем полность корзину
        Methods.deleteAll()
                .assertThat().statusCode(204);

        Methods.checkDelete("test")
                .statusCode(200)
                .body("_embedded.items", everyItem((equalTo(0))));
    }
}
