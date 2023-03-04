package Function;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;

public class Function {

    public static String getPath(List list, String name) {  //Функция чтобы получить path файла

        JSONArray jsonArray = new JSONArray(list);
        String path = "";

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("name").toString().equals(name)) {
                path = (String) jsonArray.getJSONObject(i).get("path");
                return path;
            }
        }
        return null;
    }

    public static Integer getSizeAllTrash(List list){      //Функция чтобы получить size корзины
        JSONArray jsonArray = new JSONArray(list);

        ArrayList<Integer> mySize = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++){
            if(jsonArray.getJSONObject(i).get("type").toString().equals("file"))
                mySize.add((int) jsonArray.getJSONObject(i).get("size"));
        }

        int sum = 0;
        for(int i = 0; i < mySize.size(); i++){
          sum += mySize.get(i);
        }
        return sum;
    }


    public static Integer getSizeFiles(List list,  String ...name){

        JSONArray jsonArray = new JSONArray(list);

        ArrayList<Integer> mySize = new ArrayList<>();

        for(String n : name) {
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).get("type").toString().equals("file")){
                    if(jsonArray.getJSONObject(i).get("name").toString().equals(n))
                        mySize.add((int) jsonArray.getJSONObject(i).get("size"));}
            }
        }

        int sum = 0;
        for(int i = 0; i < mySize.size(); i++){
            sum += mySize.get(i);
        }
        return sum;

    }

    public static boolean checkExistName(List list, String name){

        JSONArray jsonArray = new JSONArray(list);
        boolean check = false;

        for (int i = 0; i < jsonArray.length(); i++) {
            if (jsonArray.getJSONObject(i).get("name").toString().equals(name)) {
                check = true;
                return check;
            }
        }
        return check;
    }
}