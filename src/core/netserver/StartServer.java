package core.netserver;

import configuration.AllConfiguration;
import game.playerino.equipment.ItemsManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class StartServer {
    public static void main(String[] args) {
        //加载配置
        ApplicationContext context = new AnnotationConfigApplicationContext(AllConfiguration.class);

//        //用户模块
//        UserManager userManager = context.getBean(UserManager.class);
//        System.out.println(userManager.register("xuren2","dddd"));
//        System.out.println(userManager.login("xuren2","dddd"));
//        context.getBean(SimpleApplicationEventMulticaster.class).addListener(userManager);
//        ItemsManager itemsManager = context.getBean(ItemsManager.class);
////        List<ItemEntity> entitys = itemsManager.findItemsByPlayerId(10001);
////        for (ItemEntity i :entitys){
////            System.out.println(i.getAttribute());
////        }
//        ItemData itemData = context.getBean(ItemData.class);
//        System.out.println(itemData.jsonArray.toString());
//        context.getBean(Server.class).start();
        ItemsManager itemsManager = new ItemsManager();
        itemsManager.addItemByDefId(10009,4);
    }

}
