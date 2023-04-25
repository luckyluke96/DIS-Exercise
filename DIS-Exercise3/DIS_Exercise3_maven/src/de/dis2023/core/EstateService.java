package de.dis2023.core;

import java.util.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import de.dis2023.data.House;
import de.dis2023.data.Estate;
import de.dis2023.data.PurchaseContract;
import de.dis2023.data.EstateAgent;
import de.dis2023.data.TenancyContract;
import de.dis2023.data.Person;
import de.dis2023.data.Apartment;
import org.hibernate.query.Query;

/**
 *  Class for managing all database entities.
 *  
 * 
 * TODO: All data is currently stored in memory. 
 * The aim of the exercise is to gradually outsource data management to the 
 * database. When the work is done, all sets in this class become obsolete!
 */
public class EstateService {
	//TODO All these sets should be commented out after successful implementation.
	private Set<EstateAgent> estateAgents = new HashSet<EstateAgent>();
	private Set<Person> persons = new HashSet<Person>();
	private Set<House> houses = new HashSet<House>();
	private Set<Apartment> apartments = new HashSet<Apartment>();
	private Set<TenancyContract> tenancyContracts = new HashSet<TenancyContract>();
	private Set<PurchaseContract> purchaseContracts = new HashSet<PurchaseContract>();
	
	//Hibernate Session
	private SessionFactory sessionFactory;
	
	public EstateService() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
	}
	
	/**
	 * Find an estate agent with the given id
	 * @param id The ID of the agent
	 * @return Agent with ID or null
	 */
	public EstateAgent getEstateAgentByID(int id) {
		EstateAgent agent = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			agent = (EstateAgent) session.get(EstateAgent.class, id);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return agent;
	}
	
	/**
	 * Find estate agent with the given login.
	 * @param login The login of the estate agent
	 * @return Estate agent with the given ID or null
	 */
	public EstateAgent getEstateAgentByLogin(String login) {

		EstateAgent agent = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			String queryStr = "FROM EstateAgent e where e.login='" + login + "'";
			Query query = session.createQuery(queryStr);
			List results = query.list();
			session.getTransaction().commit();

			agent = (EstateAgent) results.get(0);
			// System.out.println(agent.getAddress());
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return agent;
	}
	
	/**
	 * Returns all estateAgents
	 */
	public Set<EstateAgent> getAllEstateAgents() {
		// return estateAgents;

		Session session = sessionFactory.getCurrentSession();
		List<EstateAgent> estateAgentList = new ArrayList<EstateAgent>();
		Set<EstateAgent> estateAgentSet = new HashSet<EstateAgent>();
		try {
			session.beginTransaction();
			String queryStr = "FROM EstateAgent";
			Query query = session.createQuery(queryStr);
			estateAgentList = query.list();
			estateAgentSet = new HashSet<EstateAgent>(estateAgentList);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			session.close();
		}

		return estateAgentSet;
	}
	
	/**
	 * Find an person with the given id
	 * @param id The ID of the person
	 * @return Person with ID or null
	 */
	public Person getPersonById(int id) {
		Person person = null;
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			person = (Person) session.get(Person.class, new Integer(id));
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return person;
	}
	
	/**
	 * Adds an estate agent
	 * @param ea The estate agent
	 */
	public void addEstateAgent(EstateAgent ea) {
		//estateAgents.add(ea);
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(ea);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Deletes an estate agent
	 * @param ea The estate agent
	 */
	public void deleteEstateAgent(EstateAgent ea) {
		//estateAgents.remove(ea);
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.delete(ea);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Adds a person
	 * @param p The person
	 */
	public void addPerson(Person p) {
		// Lukas
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(p);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Returns all persons
	 */
	public Set<Person> getAllPersons() {
		Session session = sessionFactory.getCurrentSession();
		List<Person> personList = new ArrayList<Person>();
		Set<Person> personSet = new HashSet<Person>();
		try {
			session.beginTransaction();
			String queryStr = "FROM Person";
			Query query = session.createQuery(queryStr);
			personList = query.list();
			personSet = new HashSet<Person>(personList);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			// handle exception here
		} finally {
			session.close();
		}

		return personSet;
	}
	
	/**
	 * Deletes a person
	 * @param p The person
	 */
	public void deletePerson(Person p) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.delete(p);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Adds a house
	 * @param h The house
	 */
	public void addHouse(House h) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(h);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Returns all houses of an estate agent
	 * @param ea the estate agent
	 * @return All houses managed by the estate agent
	 */
	public Set<House> getAllHousesForEstateAgent(EstateAgent ea) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		Set<House> houseSet = new HashSet<House>();
		List<House> houseList = new ArrayList<House>();
		try {
			session.beginTransaction();
			String queryStr = "FROM House h WHERE h.manager.id = " + ea.getId();
			Query query = session.createQuery(queryStr);
			//estateAgentList = query.list();
			//estateAgentSet = new HashSet<EstateAgent>(estateAgentList);
			houseList = query.list();
			houseSet = new HashSet<House>(houseList);
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return houseSet;
	}
	
	/**
	 * Find a house with a given ID
	 * @param  id the house id
	 * @return The house or null if not found
	 */
	public House getHouseById(int id) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		House house = null;
		try {
			session.beginTransaction();
			String queryStr = "FROM House h WHERE id = " + id;
			Query query = session.createQuery(queryStr);
			List results = query.list();

			house = (House) results.get(0);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return house;
	}
	
	/**
	 * Deletes a house
	 * @param h The house
	 */
	public void deleteHouse(House h) {
		// lukas
		//System.out.println("id is" + h.getId());
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.delete(h);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Adds an apartment
	 * @param w the aparment
	 */
	public void addApartment(Apartment w) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(w);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Returns all apartments of an estate agent
	 * @param ea The estate agent
	 * @return All apartments managed by the estate agent
	 */
	public Set<Apartment> getAllApartmentsForEstateAgent(EstateAgent ea) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		Set<Apartment> apartmentSet = new HashSet<Apartment>();
		List<Apartment> apartmentList = new ArrayList<Apartment>();
		try {
			session.beginTransaction();
			String queryStr = "FROM Apartment a WHERE a.manager.id = " + ea.getId();
			Query query = session.createQuery(queryStr);
			//estateAgentList = query.list();
			//estateAgentSet = new HashSet<EstateAgent>(estateAgentList);
			apartmentList = query.list();
			apartmentSet = new HashSet<Apartment>(apartmentList);
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return apartmentSet;
	}
	
	/**
	 * Find an apartment with given ID
	 * @param id The ID
	 * @return The apartment or zero, if not found
	 */
	public Apartment getApartmentByID(int id) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		Apartment apartment = null;
		try {
			session.beginTransaction();
			String queryStr = "FROM Apartment a WHERE id = " + id;
			Query query = session.createQuery(queryStr);
			List results = query.list();

			apartment = (Apartment) results.get(0);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return apartment;
	}
	
	/**
	 * Deletes an apartment
	 * @param w The apartment
	 */
	public void deleteApartment(Apartment w) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.delete(w);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	
	/**
	 * Adds a tenancy contract
	 * @param t The tenancy contract
	 */
	public void addTenancyContract(TenancyContract t) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(t);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Adds a purchase contract
	 * @param p The purchase contract
	 */
	public void addPurchaseContract(PurchaseContract p) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			session.save(p);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	/**
	 * Finds a tenancy contract with a given ID
	 * @param id Die ID
	 * @return The tenancy contract or zero if not found
	 */
	public TenancyContract getTenancyContractByID(int id) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		TenancyContract t = null;
		try {
			session.beginTransaction();
			String queryStr = "FROM TenancyContract t WHERE id = " + id;
			Query query = session.createQuery(queryStr);
			List results = query.list();

			t = (TenancyContract) results.get(0);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return t;
	}
	
	/**
	 * Finds a purchase contract with a given ID
	 * @param id The id of the purchase contract
	 * @return The purchase contract or null if not found
	 */
	public PurchaseContract getPurchaseContractById(int id) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		PurchaseContract p = null;
		try {
			session.beginTransaction();
			String queryStr = "FROM PurchaseContract t WHERE id = " + id;
			Query query = session.createQuery(queryStr);
			List results = query.list();

			p = (PurchaseContract) results.get(0);
			session.getTransaction().commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return p;
	}
	
	/**
	 * Returns all tenancy contracts for apartments of the given estate agent
	 * @param ea The estate agent
	 * @return All contracts belonging to apartments managed by the estate agent
	 */
	public Set<TenancyContract> getAllTenancyContractsForEstateAgent(EstateAgent ea) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		Set<TenancyContract> tenancyContractSet = new HashSet<TenancyContract>();
		List<TenancyContract> tenancyContractList = new ArrayList<TenancyContract>();
		try {
			session.beginTransaction();
			String queryStr = "FROM TenancyContract t WHERE t.apartment.manager.id = " + ea.getId();

			Query query = session.createQuery(queryStr);
			//estateAgentList = query.list();
			//estateAgentSet = new HashSet<EstateAgent>(estateAgentList);
			tenancyContractList = query.list();
			tenancyContractSet = new HashSet<TenancyContract>(tenancyContractList);

			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}

		return tenancyContractSet;
	}
	
	/**
	 * Returns all purchase contracts for houses of the given estate agent
	 * @param ea The estate agent
	 * @return All purchase contracts belonging to houses managed by the given estate agent
	 */
	public Set<PurchaseContract> getAllPurchaseContractsForEstateAgent(EstateAgent ea) {
		// lukas
		Session session = sessionFactory.getCurrentSession();
		Set<PurchaseContract> purchaseContractSet = new HashSet<PurchaseContract>();
		List<PurchaseContract> purchaseContractList = new ArrayList<PurchaseContract>();
		try {
			session.beginTransaction();
			String queryStr = "FROM PurchaseContract p WHERE p.house.manager.id = " + ea.getId();
			Query query = session.createQuery(queryStr);
			//estateAgentList = query.list();
			//estateAgentSet = new HashSet<EstateAgent>(estateAgentList);
			purchaseContractList = query.list();
			purchaseContractSet = new HashSet<PurchaseContract>(purchaseContractList);
			session.getTransaction().commit();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return purchaseContractSet;
	}
	
	/**
	 * Finds all tenancy contracts relating to the apartments of a given estate agent	 
	 * @param ea The estate agent
	 * @return set of tenancy contracts
	 */
	public Set<TenancyContract> getTenancyContractByEstateAgent(EstateAgent ea) {
		Set<TenancyContract> ret = new HashSet<TenancyContract>();
		Iterator<TenancyContract> it = tenancyContracts.iterator();

		while(it.hasNext()) {
			TenancyContract mv = it.next();

			if(mv.getApartment().getManager().getId() == ea.getId())
				ret.add(mv);
		}

		return ret;
	}
	
	/**
	 * Finds all purchase contracts relating to the houses of a given estate agent
	 * @param  ea The estate agent
	 * @return set of purchase contracts
	 */
	public Set<PurchaseContract> getPurchaseContractByEstateAgent(EstateAgent ea) {
		Set<PurchaseContract> ret = new HashSet<PurchaseContract>();
		Iterator<PurchaseContract> it = purchaseContracts.iterator();
		
		while(it.hasNext()) {
			PurchaseContract k = it.next();
			
			if(k.getHouse().getManager().getId() == ea.getId())
				ret.add(k);
		}
		
		return ret;
	}

	
	/**
	 * Deletes a tenancy contract
	 * @param tc the tenancy contract
	 */
	public void deleteTenancyContract(TenancyContract tc) {
		apartments.remove(tc);
	}
	
	/**
	 * Deletes a purchase contract
	 * @param pc the purchase contract
	 */
	public void deletePurchaseContract(PurchaseContract pc) {
		apartments.remove(pc);
	}
	
	/**
	 * Adds some test data
	 */
	public void addTestData() {
		//Hibernate Session erzeugen
		Session session = sessionFactory.openSession();
		
		session.beginTransaction();
		
		EstateAgent m = new EstateAgent();
		m.setName("Max Mustermann");
		m.setAddress("Am Informatikum 9");
		m.setLogin("max");
		m.setPassword("max");
		
		//TODO: This estate agent is kept in memory and the DB
		this.addEstateAgent(m);
		session.save(m);
		session.getTransaction().commit();

		session.beginTransaction();
		
		Person p1 = new Person();
		p1.setAddress("Informatikum");
		p1.setName("Mustermann");
		p1.setFirstname("Erika");
		
		
		Person p2 = new Person();
		p2.setAddress("Reeperbahn 9");
		p2.setName("Albers");
		p2.setFirstname("Hans");
		
		session.save(p1);
		session.save(p2);
		
		//TODO: These persons are kept in memory and the DB
		this.addPerson(p1);
		this.addPerson(p2);
		session.getTransaction().commit();
		
		
		session.beginTransaction();
		House h = new House();
		h.setCity("Hamburg");
		h.setPostalcode(22527);
		h.setStreet("Vogt-Kölln-Street");
		h.setStreetnumber("2a");
		h.setSquareArea(384);
		h.setFloors(5);
		h.setPrice(10000000);
		h.setGarden(true);
		h.setManager(m);
		
		session.save(h);
		
		//TODO: This house is held in memory and the DB
		this.addHouse(h);
		session.getTransaction().commit();
		
		// Create Hibernate Session
		session = sessionFactory.openSession();
		session.beginTransaction();
		EstateAgent m2 = (EstateAgent)session.get(EstateAgent.class, m.getId());
		Set<Estate> immos = m2.getEstates();
		Iterator<Estate> it = immos.iterator();
		
		while(it.hasNext()) {
			Estate i = it.next();
			System.out.println("Estate: "+i.getCity());
		}
		session.close();
		
		Apartment w = new Apartment();
		w.setCity("Hamburg");
		w.setPostalcode(22527);
		w.setStreet("Vogt-Kölln-Street");
		w.setStreetnumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setKitchen(true);
		w.setBalcony(false);
		w.setManager(m);
		this.addApartment(w);
		
		w = new Apartment();
		w.setCity("Berlin");
		w.setPostalcode(22527);
		w.setStreet("Vogt-Kölln-Street");
		w.setStreetnumber("3");
		w.setSquareArea(120);
		w.setFloor(4);
		w.setRent(790);
		w.setKitchen(true);
		w.setBalcony(false);
		w.setManager(m);
		this.addApartment(w);
		
		PurchaseContract pc = new PurchaseContract();
		pc.setHouse(h);
		pc.setContractPartner(p1);
		pc.setContractNo(9234);
		pc.setDate(new Date(System.currentTimeMillis()));
		pc.setPlace("Hamburg");
		pc.setNoOfInstallments(5);
		pc.setIntrestRate(4);

		this.addPurchaseContract(pc);
		
		TenancyContract tc = new TenancyContract();
		tc.setApartment(w);
		tc.setContractPartner(p2);
		tc.setContractNo(23112);
		tc.setDate(new Date(System.currentTimeMillis()-1000000000));
		tc.setPlace("Berlin");
		tc.setStartDate(new Date(System.currentTimeMillis()));
		tc.setAdditionalCosts(65);
		tc.setDuration(36);
		this.addTenancyContract(tc);
	}
}
