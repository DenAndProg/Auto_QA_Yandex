package Methods;

import Function.Function;
import MyData.EndPointsApi;
import io.restassured.response.ValidatableResponse;
import Specs.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.core.IsEqual.equalTo;


public class Methods {

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

    public static ValidatableResponse checkFile2(String name){
        ValidatableResponse response = Specs.requestSpecification()
                .param("path", name)
                .when()
                .request("GET", EndPointsApi.fileAndFolder)
                .then().log().all();
        return response;
    }

    public static ValidatableResponse deleteFile(String name) throws InterruptedException {
        int time = 0;
        while(time < 10) {
            ValidatableResponse response = Specs.requestSpecification()
                    .param("path", name)
                    .when()
                    .request("DELETE", EndPointsApi.fileAndFolder)
                    .then()
                    .assertThat();
            if(response.extract().statusCode() == 204) { response.log().all(); return response; }
            else{
                Thread.sleep(2000);
                time++;
            }
        }
        return null;
    }

    public static String checkDeleteFile(String name){

        List list = infoDelete();
        return Function.getPath(list, name);

    }

    public static List infoDelete(){
        return Specs.requestSpecification()
                .param("path", "/")
                .when()
                .request("GET", EndPointsApi.trashResources)
                .then()
                .extract().response().jsonPath()
                .get("_embedded.items");
    }
    public static Integer checkDeleteFile(){

        List list = infoDelete();
        return Function.getSize(list);

    }

    public static Integer checkDelete(String name, String name1){

        List list = infoDelete();
        return Function.getSize(list, name, name1);
    }

    public static void restoreTrash(String filePath){
        Specs.requestSpecification()
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
