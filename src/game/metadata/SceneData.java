package game.metadata;

import com.mchange.io.FileUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class SceneData {
    public static JSONArray jsonArray;
    public static JSONObject[] jsonObjects;
    public SceneData(){
        InputStream in = getClass().getClassLoader().getResourceAsStream("scenes.json");
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
//        String path = getClass().getClassLoader().getResource("scenes.json").toString();
//        path = path.replace("\\", "/");
//        if (path.contains(":")) {
//            path = path.replace("file:/","");// 2
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
}
