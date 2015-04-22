package com.example.xml.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Xml;
import android.widget.Toast;

import com.example.xml.domain.Person;

/**
 * ����������һ��XML�ļ�
 * 
 * @author Administrator
 *
 */
public class PersonService {
	// ��ô����assets�е��ļ���Ҫ����assets���ļ�Ҫ����context.getAssets();�õ�AssetsManager
	private Context context;

	// ����һ�����캯������ʼ����ʱ��һ��Ҫ�������ĸ����ݽ���
	public PersonService(Context context) {
		this.context = context;
	}

	/**
	 * ��person.xml�ļ�������������ת����list����
	 * 
	 * @param fileҪ�������ļ�
	 * @return
	 */
	public List<Person> getPersons(String file) {
		// ��ȡassetsManager���ǵ�ǰ������ʲ�������������
		AssetManager manager = context.getAssets();
		try {
			// ��AssetsManager��һ���ļ�����ȡ�ļ���������
			InputStream is = manager.open(file);
			// ��Android��ʹ��pull����xnl�ļ�
			// 1.��ȡpull��������ʵ��
			XmlPullParser parser = Xml.newPullParser();
			// 2.���ý�������һЩ�����������Ѿ�ָ���ļ��Ŀ�ͷ
			parser.setInput(is, "utf-8");
			// ��ȡpull��������Ӧ���¼����ͣ�type�����ж�xml�ļ��Ƿ����
			int type = parser.getEventType();
			Person person = null;
			List<Person> persons = new ArrayList<Person>();
			// �ж��ǽ������ĵ���ĩβ����������ļ���ĩβ��һֱ�Ľ�����ȥ
			while (type != XmlPullParser.END_DOCUMENT) {
				// ����� ��ʼ��ǩ
				if (type == XmlPullParser.START_TAG) {

					// getname ��ȡ��ǩ��ֵ
					if ("person".equals(parser.getName())) {
						person = new Person();
						// ȡ��ǩ �����ֵ�����磺"<person id=17 /person>"ȥ���id
						int id = Integer.parseInt(parser.getAttributeValue(0));
						person.setId(id);
					}
					// getname ��ȡ��ǩ��ֵ
					else if ("name".equals(parser.getName())) {
						// ȡ��ǩ�е�ֵ ���磺"<name>allen</name>"
						String name = parser.nextText();
						person.setName(name);
					} else if ("age".equals(parser.getName())) {
						int age = Integer.parseInt(parser.nextText());
						person.setAge(age);
					}
				}
				// �ж��ǲ��ǽ������ļ����ĩβ
				if (type == XmlPullParser.END_TAG) {
					if ("person".equals(parser.getName())) {
						persons.add(person);
						person = null;
					}
				}
				// ������һ��
				type = parser.next();
			}
			return persons;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "��ȡxml�ļ�ʧ��", Toast.LENGTH_LONG).show();
			return null;
		}
	}
	/**
	 * ����һ��xml�ļ�
	 * @param persons���������Ԫ�صĶ���
	 * @return �����ɹ�����һ��true ������ʧ�ܷ���һ��false
	 */
	public boolean savePersonToXml(List<Person> persons){
		try {
			//����һ��xml�����л���
			XmlSerializer serializer = Xml.newSerializer();
			//����ʵ��һ����Ҫ�ж�SD�Ƿ���ڣ�����/sdcard/person/person.xml
			//��ȡ�ⲿ�洢����Ŀ¼��Ҫ����SD��Ȩ�ޣ������ʹ�����һ���ļ�����
			File file = new File(Environment.getExternalStorageDirectory(),"person.xml");
			//��ʼ����һ���ļ�����ȡ�ļ���������
			FileOutputStream fos = new FileOutputStream(file);
			//��ʼ����������������ļ��� ����ķ�ʽ�����ȥ
			serializer.setOutput(fos,"utf-8");
			//дxml�ļ���ͷtrue��ʾһ��������һ���ļ���Ȼ��϶����ļ���ĩβ
			serializer.startDocument("utf-8", true);
			//��ָ�������ռ䡣������person�����ǿ�ʼ�ڵ㣬�϶��н����ڵ�
			serializer.startTag(null, "persons");
			for(Person person : persons){
				//��ʼ�ڵ�
				serializer.startTag(null, "person");
				//����ʼ�ڵ� "����" ��Ӷ���
				serializer.attribute(null, "id", person.getId()+"");
				
				//�⿪ʼ�ڵ�
				serializer.startTag(null, "name");
				//���������ڵ�֮�������Ϣ
				serializer.text(person.getName());
				//������ڵ�
				serializer.endTag(null, "name");
				
				//���ǿ�ʼ�ڵ�
				serializer.startTag(null, "age");
				//���������ڵ�	"֮��" �����Ϣ
				serializer.text(person.getAge()+"");
				//���ǽ����ڵ�
				serializer.endTag(null, "age");
				
				
				//�����ڵ�
				serializer.endTag(null, "person");
				
			}
			
			//���ǽ����ڵ�
			serializer.endTag(null, "persons");

			//�����ļ���ĩβ
			serializer.endDocument();
			fos.flush();
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		
	}
}
