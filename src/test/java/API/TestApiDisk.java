package API;

import Methods.*;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;


import static MyData.EndPointsApi.urlFile;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestApiDisk {

    public static String nameFolder = "MyFile";
    public static String nameFile = "Test.txt";
    public static String dirToFile = nameFolder + "/" + nameFile;
    public static String dirToFile2 = nameFolder + "/" + "file.txt";


    @Test
    @DisplayName("Тест 1")
    public void firstTest() throws InterruptedException {

        //Создаем папку
        Methods.createFolder(nameFolder)
                .statusCode(201);

        //Проверяем что папка существует
        Methods.checkFile(nameFolder)
                .statusCode(200)
                .body("name", (equalTo(nameFolder)));

        //Удаляем папку
        Methods.deleteFile(nameFolder, true)
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
        Methods.deleteFile(dirToFile, true)
                .statusCode(204);

        //Удаляем папку
        Methods.deleteFile(nameFolder, false)
                .statusCode(204);

    }

   @Test
    public void thirtyTest() throws InterruptedException {

        //Создаем папку
        Methods.createFolder(nameFolder)
                .statusCode(201);

        //Создаем файл
        Methods.createFile(dirToFile, urlFile)
                .statusCode(202);

        //Удаляем файл
        Methods.deleteFile(dirToFile, false)
                .statusCode(204);

        //Восстанавлием файл
        Methods.restoreTrash(Methods.checkDeleteFile(nameFile))
                .statusCode(201);

       //Проверяем что файл существует
       Methods.checkFile(dirToFile)
               .statusCode(200)
               .body("name", (equalTo(nameFile)));

       //Удаляем папку
       Methods.deleteFile(nameFolder, false)
               .statusCode(202);
    }

    @Test
    public void fourthTest() throws InterruptedException {

        //Создаем папку
        Methods.createFolder(nameFolder)
                .statusCode(201);

        //Создаем файл
        Methods.createFile(dirToFile, urlFile)
                .statusCode(202);

        //Создаем второй файл
        Methods.createFile(dirToFile2, urlFile)
                .statusCode(202);

        //Получаем размер корзины до удаления файлов
        Integer beforeSizeDustbin = Methods.checkDeleteFile();

        //Удаляем первый файл
        Methods.deleteFile(dirToFile, false)
                .statusCode(204);

        //Удаляем второй файл
        Methods.deleteFile(dirToFile2, false)
                .statusCode(204);

        //Получаем размер двух файлов
        Integer s4 = Methods.checkDelete("Test.txt", "file.txt");

        //Получаеем размер корзины
        Integer sizeDustbin = Methods.checkDeleteFile();

        //Проверяем что (размер корзины = размер двух файлов + размер корзины без двух файлов)
        assert(sizeDustbin == s4 + beforeSizeDustbin); //в корзине могут быть два файла с одинаковым названием

        //Восстанавлиаем первый файл
        Methods.restoreTrash(Methods.checkDeleteFile(nameFile))
                .statusCode(201);

        //Восстанавливаем второй файл
        Methods.restoreTrash(Methods.checkDeleteFile("file.txt"))
                .statusCode(201);

        //Удаляем папку с файлами
        Methods.deleteFile(nameFolder, true)
                .statusCode(202);
    }

    @Test
    public void fiftiethTest() throws InterruptedException {

        //Создаем папку test
        Methods.createFolder("test")
                .statusCode(201);

        //В папке test создаем папку foo
        Methods.createFolder("test/foo")
                .statusCode(201);

        //В папке foo создаем файл autotest.txt
        Methods.createFile("test/foo/autotest.txt", urlFile)
                .statusCode(202);

        //Сравниваем методанные папки test
        Methods.checkFile("test")
                .statusCode(200)
                .body("_embedded.items.name", everyItem((equalTo("foo"))))
                .body("_embedded.items.path", everyItem((equalTo("disk:/test/foo"))));

        //Удаляем папку test, помещая ее в корзину
        Methods.deleteFile("test", false)
                .statusCode(202);

        //Проверяем что в корзине есть папка test
        Methods.checkDelete("test")
                .statusCode(200);
    }

    @Test
    public void sixtiethTest() throws InterruptedException {

        //Создаем папку test
        Methods.createFolder("test")
                .statusCode(201);

        //Создаем папку foo в папке test
        Methods.createFolder("test/foo")
                .statusCode(201);

        //Создаем файл autotest.txt в папке foo
        Methods.createFile("test/foo/autotest.txt", urlFile)
                .statusCode(202);

        //Удаляем папку test
        Methods.deleteFile("test", false)
                .statusCode(202);

        //Очищаем полность корзину
        Methods.deleteAll()
                .assertThat().statusCode(202);

        //Проверяем что наша корзина пуста
        Methods.checkDelete("test")
                .statusCode(200)
                .body("total", ((equalTo(null))));
    }
}
