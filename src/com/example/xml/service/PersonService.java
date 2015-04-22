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
 * 解析和生成一个XML文件
 * 
 * @author Administrator
 *
 */
public class PersonService {
	// 怎么访问assets中的文件？要访问assets的文件要调用context.getAssets();得到AssetsManager
	private Context context;

	// 定义一个构造函数，初始化的时候一定要把上下文给传递进来
	public PersonService(Context context) {
		this.context = context;
	}

	/**
	 * 把person.xml文件的输入流解释转化成list集合
	 * 
	 * @param file要解析的文件
	 * @return
	 */
	public List<Person> getPersons(String file) {
		// 获取assetsManager就是当前软件的资产包管理服务对象
		AssetManager manager = context.getAssets();
		try {
			// 用AssetsManager打开一个文件并获取文件的输入流
			InputStream is = manager.open(file);
			// 在Android下使用pull解析xnl文件
			// 1.获取pull解释器的实例
			XmlPullParser parser = Xml.newPullParser();
			// 2.设置解析器的一些参数，这是已经指向文件的开头
			parser.setInput(is, "utf-8");
			// 获取pull解析器对应的事件类型，type用来判断xml文件是否结束
			int type = parser.getEventType();
			Person person = null;
			List<Person> persons = new ArrayList<Person>();
			// 判断是解析到文档的末尾，如果不是文件的末尾就一直的解释下去
			while (type != XmlPullParser.END_DOCUMENT) {
				// 如果是 开始标签
				if (type == XmlPullParser.START_TAG) {

					// getname 是取标签的值
					if ("person".equals(parser.getName())) {
						person = new Person();
						// 取标签 里面的值，例如："<person id=17 /person>"去这个id
						int id = Integer.parseInt(parser.getAttributeValue(0));
						person.setId(id);
					}
					// getname 是取标签的值
					else if ("name".equals(parser.getName())) {
						// 取标签中的值 例如："<name>allen</name>"
						String name = parser.nextText();
						person.setName(name);
					} else if ("age".equals(parser.getName())) {
						int age = Integer.parseInt(parser.nextText());
						person.setAge(age);
					}
				}
				// 判断是不是解析到文件你的末尾
				if (type == XmlPullParser.END_TAG) {
					if ("person".equals(parser.getName())) {
						persons.add(person);
						person = null;
					}
				}
				// 解析下一行
				type = parser.next();
			}
			return persons;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(context, "获取xml文件失败", Toast.LENGTH_LONG).show();
			return null;
		}
	}
	/**
	 * 生成一个xml文件
	 * @param persons往里面添加元素的对象
	 * @return 创建成功返回一个true ，创建失败返回一个false
	 */
	public boolean savePersonToXml(List<Person> persons){
		try {
			//创建一个xml的序列化器
			XmlSerializer serializer = Xml.newSerializer();
			//（其实这一步还要判断SD是否存在），在/sdcard/person/person.xml
			//获取外部存储器的目录，要加入SD的权限，这样就创建了一个文件对象
			File file = new File(Environment.getExternalStorageDirectory(),"person.xml");
			//开始创建一个文件，获取文件的输入流
			FileOutputStream fos = new FileOutputStream(file);
			//初始化这个序列器，把文件和 编码的方式给输进去
			serializer.setOutput(fos,"utf-8");
			//写xml文件的头true表示一个独立的一个文件，然后肯定有文件的末尾
			serializer.startDocument("utf-8", true);
			//不指定命名空间。名字是person，这是开始节点，肯定有结束节点
			serializer.startTag(null, "persons");
			for(Person person : persons){
				//开始节点
				serializer.startTag(null, "person");
				//往开始节点 "里面" 添加东西
				serializer.attribute(null, "id", person.getId()+"");
				
				//这开始节点
				serializer.startTag(null, "name");
				//往这两个节点之间添加信息
				serializer.text(person.getName());
				//这结束节点
				serializer.endTag(null, "name");
				
				//这是开始节点
				serializer.startTag(null, "age");
				//往这两个节点	"之间" 添加信息
				serializer.text(person.getAge()+"");
				//这是结束节点
				serializer.endTag(null, "age");
				
				
				//结束节点
				serializer.endTag(null, "person");
				
			}
			
			//这是结束节点
			serializer.endTag(null, "persons");

			//这是文件的末尾
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
