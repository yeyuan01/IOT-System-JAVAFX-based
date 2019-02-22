package ca.uvic.seng330.assn2.user;

import ca.uvic.seng330.assn2.part1.HubRegistrationException;
import ca.uvic.seng330.assn2.part1.Mediator;
import ca.uvic.seng330.assn3.adminMenu.AdminController;

public class BasicUser extends User {
	private final Mediator aMed;
	public BasicUser(Mediator pMed,AdminController ac, String pUsername, String pPassword) {
		super(pMed,ac,pUsername, pPassword);
		aMed = pMed;
		try {
	      aMed.register(this);
	    } catch (HubRegistrationException e) {
	      e.printStackTrace();
	    }
	}
}

