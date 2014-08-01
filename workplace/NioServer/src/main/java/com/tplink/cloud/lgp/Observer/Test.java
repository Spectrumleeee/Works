package com.tplink.cloud.lgp.Observer;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConcreteObserver coa = new ConcreteObserver("A");
		ConcreteObserver cob = new ConcreteObserver("B");

		// create five subjects
		ConcreteSubject csaa = new ConcreteSubject("AA");
		ConcreteSubject csbb = new ConcreteSubject("BB");
		ConcreteSubject cscc = new ConcreteSubject("CC");
		ConcreteSubject csdd = new ConcreteSubject("DD");
		ConcreteSubject csee = new ConcreteSubject("EE");
		
		coa.subscribe(csaa);
		coa.subscribe(csbb);
		coa.subscribe(cscc);
		coa.subscribe(csdd);
		cob.subscribe(csbb);
		cob.subscribe(csdd);
		cob.subscribe(csee);

		csaa.notify("hahaha");
		csbb.notify("wawawa");
		cscc.notify("gagaga");
		csee.notify("yayaya");
		csdd.notify("lalala");
		
		System.out.println(coa.getInterest());
		System.out.println(cob.getInterest());

	}

}
