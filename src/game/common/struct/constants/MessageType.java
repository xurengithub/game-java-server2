package game.common.struct.constants;

/**
 * �ͻ�����Ϣ����
 * 1xxx���������������Ϣ�� 2xxx������װ�������Ϣ��3xxx�������������Ϣ
 * 4xxx�������������Ϣ�� 5xxx������ս�������Ϣ; 6xxx: ������Ʒ�����Ϣ
 * ��һλΪ���࣬�ڶ�λΪ���࣬����λΪ������Ϣ
 */
public class MessageType {

	public static final int USER_REGISTER = 0001;
	/**��ȡ���������Ϣ*/
	public static final int ROLE_GET = 1101;
	
	/**װ����ȡ������*/
	public static final int EQUIPMENT_GET = 2100;
	
	/**װ������*/
	public static final int EQUIPMENT_UNLOCK = 2101;
	
	/**װ������*/
	public static final int EQUIPMENT_UPGRADE = 2102;
	
	/**װ��Я��*/
	public static final int EQUIPMENT_CARRY = 2103;
	
	/**װ����ʯ��Ƕ*/
	public static final int EQUIPMENT_EMBED = 2104;
	
	/**װ������*/
	public static final int EQUIPMENT_INHERIT = 2105;
	
	/**���ܻ�ȡ,������*/
	public static final int SKILL_GET = 3100;
	
	/**����ѧϰ*/
	public static final int SKILL_LEARN = 3101;
	
	/**��������*/
	public static final int SKILL_UPGRADE = 3102;
	
	/**����Я��*/
	public static final int SKILL_CARRY = 3103;
	
	/**��ȡ���*/
	public static final int PARTNER_GET = 4101;
	
	/**�������*/
	public static final int PARTNER_UPGRADE = 4102;
	
	/**�����ļ*/
	public static final int PARTNER_RECRUIT = 4103;
	
	/**��鴫��*/
	public static final int PARTNER_INHERIT = 4104;
	
	/**��鼼������*/
	public static final int PARTNER_SKILL_RESET = 4105;
	
	/**�������*/
	public static final int PARTNER_CHANGE_PARTNER = 4106;

  	/**pveս����ʼ*/
  	public static final int PVE_BATTLE_START = 5101;
  	
  	/**pveս����һ�غ�*/
  	public static final int PVE_BATTLE_NEXT_STEP = 5102;
  	
  	/**pveս��������������*/
  	public static final int PVE_BATTLE_OPEN_CHEST = 5103;
  	
  	/**pvpս����ʼ*/
  	public static final int PVP_BATTLE_START = 5201;
  	
  	/**pvpս����һ�غ�*/
  	public static final int PVP_BATTLE_NEXT_STEP = 5202;
  	
  	/**pvp����ƥ��*/
  	public static final int PVP_BATTLE_LADDER_MATCH = 5203;
  	
  	/**������ս����*/
  	public static final int PVP_ADD_LAST_TIMES = 5204;
  	
  	/**������սʱ��*/
  	public static final int PVP_RESET_CHAL_TIME = 5205;
  	
  	/**��ȡ��Ʒ��Ϣ*/
  	public static final int ITEM_GET = 6100;
  	
  	/**��ʯ�ϳ�*/
  	public static final int JEWEL_COMPOUND = 6101;
  	
  	/**���ĺϳ�*/
  	public static final int RUNE_COMPOUND = 6201;
}
