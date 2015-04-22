package com.example.xml;

import java.util.ArrayList;
import java.util.List;

import com.example.xml.domain.Person;
import com.example.xml.service.PersonService;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv_play;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_play = (TextView) findViewById(R.id.tv_play);
		
		PersonService service = new PersonService(this);
		/***********这是演示解析XML文件****************/
		List<Person> persons = service.getPersons("person.xml");
		StringBuffer sb = new StringBuffer();
		for(Person person : persons){
			String age ="年龄"+person.getAge();
			String name = person.getName();
			String id = "id"+person.getId();
			sb.append(name+""+age+""+id);
		}
		tv_play.setText(sb.toString());
		
		/*************这是演示生成一个XML文件*******************/
		List<Person> personsl =  new ArrayList<Person>();
		Person person1 = new Person(21,"张三",18);
		Person person2 = new Person(13,"李四",19);
		Person person3 = new Person(43,"王五",20);
		Person person4 = new Person(28,"刘琪",78);
		personsl.add(person1);
		personsl.add(person2);
		personsl.add(person3);
		personsl.add(person4);
		service.savePersonToXml(personsl);
		
	}

}
