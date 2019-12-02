package game.metadata;

import com.mchange.io.FileUtils;
import game.playerino.equipment.ItemDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;


@Component
public class ItemData {
    public JSONArray jsonArray;
    public JSONObject[] jsonObjects;
    public ItemData(){

        InputStream in = getClass().getClassLoader().getResourceAsStream("items.json");
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String line = reader.readLine();
            while (line !=null){
                stringBuffer.append(line);
                stringBuffer.append("\n");
                line = reader.readLine();
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String path = getClass().getClassLoader().getResource("items.json").toString();
//        System.out.println(path);
//        path = path.replace("\\", "/");
//        System.out.println(path);
//        if (path.contains(":")) {
//            path = path.replace("file:/","");// 2
//            System.out.println(path);
//        }
//        try {
//            String cfg = FileUtils.getContentsAsString(new File(path));
            String cfg = stringBuffer.toString();

        jsonArray = JSONArray.fromObject(cfg);

            jsonObjects = new JSONObject[jsonArray.size()];
            jsonArray.toArray(jsonObjects);

//        } catch (IOException e) {
//            e.printStackTrace();
//            jsonArray = null;
//        }
    }

    public JSONObject findJsonObjById(int id){
        for (int i = 0; i < jsonObjects.length; i++){
            if(jsonObjects[i].getInt("id") == id){
                return jsonObjects[i];
            }
        }
        return null;
    }
}
