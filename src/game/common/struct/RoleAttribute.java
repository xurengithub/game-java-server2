package game.common.struct;

public class RoleAttribute {

	private int id;				//id,������Ӧ��ɫ��Ӧ�ȼ�����1001������ȼ�1����ʿ
	private int hp;				//Ѫ��
	private int atk;			//����
	private int pDef;			//�������
	private int mDef;			//ħ������
	private int hit;			//����
	private int antiHit;		//����
	private int crt;			//����
	private int speed;			//�ٶ�
	
	public int getId() {
		return id;
	}
	public int getHp() {
		return hp;
	}
	public int getpDef() {
		return pDef;
	}
	public int getmDef() {
		return mDef;
	}
	public int getHit() {
		return hit;
	}
	public int getAntiHit() {
		return antiHit;
	}
	public int getCrt() {
		return crt;
	}
	public int getSpeed() {
		return speed;
	}
	public int getAtk() {
		return atk;
	}
	
	
	public void setId(int id) {
		this.id = id;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public void setpDef(int pDef) {
		this.pDef = pDef;
	}
	public void setmDef(int mDef) {
		this.mDef = mDef;
	}
	public void setHit(int hit) {
		this.hit = hit;
	}
	public void setAntiHit(int antiHit) {
		this.antiHit = antiHit;
	}
	public void setCrt(int crt) {
		this.crt = crt;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void setAtk(int atk) {
		this.atk = atk;
	}
}
