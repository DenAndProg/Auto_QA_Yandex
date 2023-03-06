package Methods;

import Function.Function;
import MyData.EndPointsApi;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import Specs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Methods {

    @Step("Создать папку")
    public static ValidatableResponse createFolder(String name){
        return Specs.requestSpecification()
        .param("path", name)
        .when()
        .request("PUT", EndPointsApi.fileAndFolder)
        .then().log().all();
    }

    public static ValidatableResponse createFile(String name, String url){
        Map<String, String> pathParams;
        pathParams = new HashMap<>();
        pathParams.put("path", "/" + name);
        pathParams.put("url", url);
       ValidatableResponse response = Specs.requestSpecification()
                .queryParams(pathParams)
                .when()
                .request("POST", EndPointsApi.fileUpload)
                .then().log().all();
        return response;
    }

    public static ValidatableResponse checkFile(String name){
       return Specs.requestSpecification()
                .param("path", name)
                .when()
                .request("GET", EndPointsApi.fileAndFolder)
                .then().log().all();
    }

    public static ValidatableResponse deleteFile(String name, boolean clear) throws InterruptedException {  //если clear = true, то удаление без переноса в корзину
        Thread.sleep(2000);
        ValidatableResponse response = Specs.requestSpecification().when()
                    .request("DELETE", EndPointsApi.fileAndFolder + "?path=" + name + "&permanently=" + clear)
                    .then().log().status();
        return response;
    }

    public static String checkDeleteFile(String name){
        List list = infoDeleteFile();
        return Function.getPath(list, name);
    }

    public static Integer checkDeleteFile(){

        List list = infoDeleteFile();
        return Function.getSizeAllTrash(list);

    }

    public static Integer checkDelete(String name, String name1){
        List list = infoDeleteFile();
        return Function.getSizeFiles(list, name, name1);

    }

    public static List infoDeleteFile(){
        return Specs.requestSpecification()
                .param("path", "/")
                .when()
                .request("GET", EndPointsApi.trashResources)
                .then()
                .extract().response().jsonPath()
                .get("_embedded.items");
    }

    public static ValidatableResponse restoreTrash(String filePath){
        return Specs.requestSpecification()
                .param("path", filePath)
                .when()
                .request("PUT", EndPointsApi.trashFile)
                .then().log().all();
    }

    public static ValidatableResponse checkDelete(String name){
        ValidatableResponse response =
                Specs.requestSpecification()
                        .param("path", "/")
                        .when()
                        .request("GET", EndPointsApi.trashResources)
                        .then();

        List list = response.extract().jsonPath().get("_embedded.items");
        boolean check = Function.checkExistName(list, name);
        assert (check);
        return response;
    }

    public static ValidatableResponse deleteAll(){
        ValidatableResponse response =
                Specs.requestSpecification()
                        .when()
                        .request("DELETE", EndPointsApi.trash)
                        .then();
        return response;
    }
}
