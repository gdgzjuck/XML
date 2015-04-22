package com.example.xml.service.test;

import java.util.List;

import com.example.xml.domain.Person;
import com.example.xml.service.PersonService;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestPersonService extends AndroidTestCase {
	private static final String TAG = "TestPersonService";
	public void testGetPerson()throws Exception{
		PersonService service = new PersonService(getContext());
		List<Person> persons = service.getPersons("person.xml");
		assertEquals(2, persons.size());
		Log.i(TAG, persons.get(0).getName());
	} 		

}
