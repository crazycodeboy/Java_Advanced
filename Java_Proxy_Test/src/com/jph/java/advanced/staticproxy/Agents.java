package com.jph.java.advanced.staticproxy;

/**
 * 代理类：用来代理和封装真实主题<br>
 * 在这里表示房产中介
 * @author Penn
 */
public class Agents implements Sales {
	private Owner owner;
	public Agents() {
	}
	@Override
	public void sell() {
		System.out.println("我是房产中介，正在核实买房者是否符合购买该房屋的资格");
		getOwner().sell();
		System.out.println("我是房产中介，正在收取提成");
	}
	private Owner getOwner() {
		if (owner==null) {
			owner=new Owner();
		}
		return owner;
	}
}
