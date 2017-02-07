package com.example.androidar;

public class Vector {
	
	public float x;
	public float y;
	public float z;
	
	//Vector ������
	public Vector() {
		x = 0f;
		y = 0f;
		z = 0f;
	}
	
	//Vector ������ ���� �޾� �ʱ�ȭ
	public Vector(float x, float y, float z) {
		set(x, y, z);
	}
	
	//Vector ������ ���� ��ü�� ���� �� ����
	public Vector(Vector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	//���� ��ü�� ���� �� �����ϴ� set �޼ҵ�
	public void set(Vector v) {
		set(v.x, v.y, v.z);
	}
	
	public void add(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
	
	public void add(Vector v) {
		add(v.x, v.y, v.z);
	}

	// ������ ��ġ�� ���� ��ü���� ��
	public void sub(float x, float y, float z) {
		add(-x, -y, -z);
	}
	
	public void sub(Vector v) {
		add(-v.x, -v.y, -v.z);
	}

	//���� �޴� set �޼ҵ�
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void prod(mMatrix m) {
		
		float xTemp = m.a1 * x + m.a2 * y + m.a3 * z;
		float yTemp = m.b1 * x + m.b2 * y + m.b3 * z;
		float zTemp = m.c1 * x + m.c2 * y + m.c3 * z;

		x = xTemp;
		y = yTemp;
		z = zTemp;
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public void scale(float s){
		x = x*s;
		y = y*s;
		z = z*s;
	}
}
