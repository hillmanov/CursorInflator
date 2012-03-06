#CursorInflator#

In short, CursorInflator will take a cursor and inflate your custom objects.

###_NOTE: The following examples assume that you are going to be working with an existing ContentProvider_###

To use, first create your model class and annotate each member field with the "Column" annotation. This tells the inflator the relationship between your object definition and the underlying db schema. 

_(The Column annotation is a public interface in the CursorInflator class file)_

The Column annotation needs only one thing - the name of the column in the database table that the field should map to. In following example, the names of the columns are provided by constants in the PersonContentProvider.

	public class Person {
		
		@Column(name=PersonContentProvider._ID)
		private long id;
		
		@Column(name=PersonContentProvider.NAME)
		private String name;
		
		@Column(name=PersonContentProvider.LAST_NAME)
		private String lastName;
		
		@Column(name=PersonContentProvider.AGE)
		private int age;

		// Getters and Setters...//
		
	}

Once you have your models setup, all you need is a cursor to inflate. The CursorInflator class has two static methods that you can call:

	*CursorInflator.inflateList
	*CursorInflator.inflateOne
	
inflateList and inflateOne both take a cursor as their first first argument, and the class of the object you wish to inflate as the second. 

For example. To get a list of Person objects:

	Cursor personCursor = getContentResolver().query(PersonContentProvider.CONTENT_URI, null, null, null, PersonContentProvider.DEFAULT_SORT_ORDER);

	List<Person> personList = CursorInflator.inflateList(personCursor, Person.class);


To get just one person:

	Uri personUri = ContentUris.withAppendedId(PersonContentProvider.CONTENT_URI, personId);
	Cursor cursor = getContentResolver().query(personUri, null, null, null, null);

	Person person = CursorInflator.inflateOne(cursor, Person.class);


