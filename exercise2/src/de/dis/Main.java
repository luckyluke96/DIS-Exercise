package de.dis;

import de.dis.data.Makler;
import de.dis.data.Person;

import java.text.Normalizer.Form;

import de.dis.data.Contract;
import de.dis.data.Estate;

/**
 * Hauptklasse
 */
public class Main {
	static Makler m_session = new Makler();

	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}

	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		// Menüoptionen
		final int MENU_MAKLER = 0;
		final int MENU_ESTATE = 1;
		final int MENU_CONTRACT = 2;
		final int QUIT = 3;

		// Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_MAKLER);
		mainMenu.addEntry("Grundstücks-Verwaltung", MENU_ESTATE);
		mainMenu.addEntry("Vertrags-Verwaltung", MENU_CONTRACT);
		mainMenu.addEntry("Beenden", QUIT);

		// Verarbeite Eingabe
		while (true) {
			int response = mainMenu.show();

			switch (response) {
				case MENU_MAKLER:
					askPassword();
					showMaklerMenu();
					break;
				case MENU_ESTATE:
					showAgentLogin();
					showEstateMenu();
					break;
				case MENU_CONTRACT:
					showContractMenu();
					break;
				case QUIT:
					return;
			}
		}
	}

	private static void showContractMenu() {
		final int INSERT_PERSON = 0;
		final int SIGN_CONTRACT = 1;
		final int OVERVIEW = 2;
		final int BACK = 3;

		Menu contractMenu = new Menu("Vertrags-Menü");
		contractMenu.addEntry("Neue Person", INSERT_PERSON);
		contractMenu.addEntry("Vertrag unterzeichnen", SIGN_CONTRACT);
		contractMenu.addEntry("Vertragsüberblick", OVERVIEW);
		contractMenu.addEntry("Zurück zum Hauptmenü", BACK);

		while (true) {
			int response = contractMenu.show();

			switch (response) {
				case INSERT_PERSON:
					newPerson();
					break;
				case SIGN_CONTRACT:
					signContract();
					break;
				case OVERVIEW:
					createOverview();
					break;
				case BACK:
					return;
			}
		}
	}

	private static void createOverview() {
		Contract c = new Contract();
		c.overview();
	}

	private static void signContract() {
		Contract c = new Contract();

		c.setContractNum(FormUtil.readInt("Vertragsnummer"));
		c.setDate(FormUtil.readString("Datum"));
		c.setPlace(FormUtil.readString("Ort"));
		c.save();
	}

	private static void newPerson() {
		Person p = new Person();

		p.setFirstName(FormUtil.readString("Vorname"));
		p.setName(FormUtil.readString("Nachname"));
		p.setAddress(FormUtil.readString("Adresse"));
		p.save();

		System.out.println("Person mit der ID " + p.getId() + " wurde erzeugt.");
	}

	public static void askPassword() {

		while (true) {
			String pw = FormUtil.readString("Passwort");

			if (pw.equals("1234")) {
				System.out.println("Passwort korrekt");
				break;
			} else {
				System.out.println("Falsches Passwort");
			}

		}
	}

	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {
		// Menüoptionen
		final int NEW_MAKLER = 0;
		final int DELETE_MAKLER = 1;
		final int UPDATE_MAKLER = 2;
		final int BACK = 3;

		// Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
		maklerMenu.addEntry("Makler löschen", DELETE_MAKLER);
		maklerMenu.addEntry("Makler aktualisieren", UPDATE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = maklerMenu.show();

			switch (response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case DELETE_MAKLER:
					deleteMakler();
					break;
				case UPDATE_MAKLER:
					updateMakler();
					break;
				case BACK:
					return;
			}
		}
	}

	/**
	 * Zeigt die Grundstücksverwaltung
	 */
	public static void showAgentLogin() {

		m_session.setLogin(FormUtil.readString("Login"));
		m_session.setPassword(FormUtil.readString("Passwort"));
		m_session.login();

	}

	public static void showEstateMenu() {
		// Estate e = new Estate();

		// Menüoptionen
		final int NEW_ESTATE = 0;
		final int DELETE_ESTATE = 1;
		final int UPDATE_ESTATE = 2;
		final int BACK = 3;

		// Maklerverwaltungsmenü
		Menu estateMenu = new Menu("Gründstücks-Verwaltung");
		estateMenu.addEntry("Neues Grundstück", NEW_ESTATE);
		estateMenu.addEntry("Grundstück löschen", DELETE_ESTATE);
		estateMenu.addEntry("Grundstück aktualisieren", UPDATE_ESTATE);
		estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

		// Verarbeite Eingabe
		while (true) {
			int response = estateMenu.show();

			switch (response) {
				case NEW_ESTATE:
					newEstate();
					break;
				case DELETE_ESTATE:
					deleteEstate();
					break;
				case UPDATE_ESTATE:
					updateEstate();
					break;
				case BACK:
					return;
			}
		}
	}

	// TODO
	private static void newEstate() {
		Estate e = new Estate();

		e.setCity(FormUtil.readString("Stadt"));
		e.setPostalCode(FormUtil.readInt("Postleitzahl"));
		e.setStreet(FormUtil.readString("Straße"));
		e.setStreetNumnber(FormUtil.readInt("Hausnummer"));
		e.setSquareArea(FormUtil.readInt("Wohnfläche in qm"));
		e.setEstateAgendID(m_session.getId());
		e.save();

		System.out.println("Grundstück mit der ID " + e.getId() + " wurde erzeugt.");
	}

	// TODO
	private static void deleteEstate() {
		Estate e = new Estate();

		e.setId(FormUtil.readInt("ID des Grundstücks"));
		e.delete();

		System.out.println("Grundstück mit ID " + e.getId() + " wurde geloescht.");
	}

	// TODO
	private static void updateEstate() {
		Estate e = new Estate();

		e.setId(FormUtil.readInt("ID des Grundstücks"));
		e.setCity(FormUtil.readString("Stadt"));
		e.setPostalCode(FormUtil.readInt("Postleitzahl"));
		e.setStreet(FormUtil.readString("Straße"));
		e.setStreetNumnber(FormUtil.readInt("Hausnummer"));
		e.setSquareArea(FormUtil.readInt("Wohnfläche in qm"));
		e.setEstateAgendID(m_session.getId());
		e.update();

		System.out.println("Grundstueck mit ID " + e.getId() + "wurde aktualisiert.");
	}

	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		Makler m = new Makler();

		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.save();

		System.out.println("Makler mit der ID " + m.getId() + " wurde erzeugt.");
	}

	public static void updateMakler() {
		Makler m = new Makler();

		m.setName(FormUtil.readString("Neuer Name"));
		m.setAddress(FormUtil.readString("Neue Adresse"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Passwort"));
		m.update();

		System.out.println("Makler mit dem Login " + m.getLogin() + " wurde aktualisiert." +
				"\nNeuer Name: " + m.getName() +
				"\nNeue Adresse: " + m.getAddress());
	}

	public static void deleteMakler() {
		Makler m = new Makler();

		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Password"));
		m.delete();

		System.out.println("Makler mit dem Login " + m.getLogin() + " wurde gelöscht.");

	}
}
