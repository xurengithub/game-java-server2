package game.metadata;

import net.sf.json.JSONObject;

import java.util.Iterator;

public class Test {
    public static void main(String[] args) {
        ItemData itemData = new ItemData();
        JSONObject o = itemData.jsonArray.getJSONObject(1);
        System.out.println(o.getString("name"));
        System.out.println(itemData.jsonArray.size());
    }
}
