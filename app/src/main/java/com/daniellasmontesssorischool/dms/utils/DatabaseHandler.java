package com.daniellasmontesssorischool.dms.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.daniellasmontesssorischool.dms.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class DatabaseHandler extends SQLiteOpenHelper {
	Context cont;
    // All Static variables
	
    // Database Version
    private static final int DATABASE_VERSION = 1;
    
    // Database Name
    private static final String DATABASE_NAME = "Dms";
    
    // All table names
    private static final String TABLE_QUIZ = "Quiz";
	private static final String TABLE_NEWS = "News";


    private static final String TABLE_EVENTS = "Events";
    private static final String TABLE_CALENDAR = "Calendar";

    private static final String TABLE_ARTICLE = "Articles";
    private static final String TABLE_ASSIGNMENT = "Assignment";

    
 // QUIZ Table Columns names
    private static final String KEY_QID = "id";
    private static final String KEY_QSUB = "subject";
    private static final String KEY_Q = "question";
    private static final String KEY_QOPTION1 = "option1";
    private static final String KEY_QOPTION2 = "option2";
    private static final String KEY_QOPTION3 = "option3";
    private static final String KEY_QOPTION4 = "option4";
    private static final String KEY_QANS = "answer";
    private static final String KEY_DIFF = "difficulty";

    private static final String KEY_NEWSID = "newsid";
    private static final String KEY_NEWSTITLE = "news_title";
    private static final String KEY_NEWSDETAIL = "news_detail";
    private static final String KEY_NEWSTYPE = "news_type";
    private static final String KEY_NEWSDATE = "news_date";

    private static final String KEY_EVENTID = "eventid";
    private static final String KEY_EVENTPARTICIPANT = "event_partip";
    private static final String KEY_EVENTHEADER = "event_header";
    private static final String KEY_EVENTDETAIL = "event_detail";
    private static final String KEY_EVENTDATE = "event_date";

    private static final String KEY_CALENID = "calenid";
    private static final String KEY_CALENDAYMONTH = "calen_daymonth";
    private static final String KEY_CALENPICTITLE = "calen_pictitle";
    private static final String KEY_CALENHEADER = "calen_header";
    private static final String KEY_CALENDETAIL = "calen_detail";
    private static final String KEY_CALENINTERVAL = "calen_interval";

    private static final String KEY_ARTICLEID = "articleid";
    private static final String KEY_ARTICLETITLE = "article_title";
    private static final String KEY_ARTICLEDATE = "article_date";
    private static final String KEY_ARTICLENAME = "article_name";
    private static final String KEY_ARTICLECLASS = "article_class";
    private static final String KEY_ARTICLECONTENT = "article_content";
    private static final String KEY_ARTICLETYPE = "article_type";

    private static final String KEY_ASSIGNMENTID = "assign_id";
    private static final String KEY_ASSIGNMENTSUBJECT = "assignment_subject";
    private static final String KEY_ASSIGNMENTTITLE = "assignment_title";
    private static final String KEY_ASSIGNMENTDETAIL = "assignment_detail";
    private static final String KEY_ASSIGNMENTSUBM = "assignment_subm";
    private static final String KEY_TEACHER = "assignment_teacher";
    private static final String KEY_ASSIGNMENTCLASS = "assignment_class";
    private static final String KEY_ASSIGNMENTEXTRA = "assignment_extra";

    private static final String FI_SUB = "subject";
    private static final String FI_QUESTION = "question";

    private static final String FI_OPT1 = "option1";
    private static final String FI_OPT2 = "option2";
    private static final String FI_OPT3 = "option3";
    private static final String FI_OPT4 = "option4";

    private static final String FI_QANS = "answer";
    private static final String FI_DIFF = "difficulty";
    
    private SQLiteDatabase db;

    public String[][] rows;
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.cont = context;
        db = this.getWritableDatabase();
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

     //0 for easy, 1 for intermediate, 2 for hard
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZ + "("
                + KEY_QID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_QSUB + " TEXT,"
                + KEY_Q + " TEXT NOT NULL,"
                + KEY_QOPTION1 + " TEXT NOT NULL,"
                + KEY_QOPTION2 + " TEXT NOT NULL,"
                + KEY_QOPTION3 + " TEXT NOT NULL,"
                + KEY_QOPTION4 + " TEXT NOT NULL,"
                + KEY_DIFF + " TEXT,"
                + KEY_QANS + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_QUIZ_TABLE);

        String CREATE_NEWS_TABLE = "CREATE TABLE " + TABLE_NEWS + "(" //PRIMARY KEY AUTOINCREMENT
                + KEY_NEWSID + " INTEGER NOT NULL,"
                + KEY_NEWSTITLE + " TEXT NOT NULL,"
                + KEY_NEWSDETAIL + " TEXT NOT NULL,"
                + KEY_NEWSTYPE + " TEXT NOT NULL,"
                + KEY_NEWSDATE + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_NEWS_TABLE);

        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" //PRIMARY KEY AUTOINCREMENT
                + KEY_EVENTID + " INTEGER NOT NULL,"
                + KEY_EVENTPARTICIPANT + " TEXT NOT NULL,"
                + KEY_EVENTHEADER + " TEXT NOT NULL,"
                + KEY_EVENTDETAIL + " TEXT NOT NULL,"
                + KEY_EVENTDATE + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_EVENTS_TABLE);

        String CREATE_CALENDAR_TABLE = "CREATE TABLE " + TABLE_CALENDAR + "(" //PRIMARY KEY AUTOINCREMENT
                + KEY_CALENID + " INTEGER NOT NULL,"
                + KEY_CALENPICTITLE + " TEXT NOT NULL,"
                + KEY_CALENDAYMONTH + " TEXT NOT NULL,"
                + KEY_CALENHEADER + " TEXT NOT NULL,"
                + KEY_CALENDETAIL + " TEXT NOT NULL,"
                + KEY_CALENINTERVAL + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_CALENDAR_TABLE);

        String CREATE_ARTICLE_TABLE = "CREATE TABLE " + TABLE_ARTICLE + "(" //PRIMARY KEY AUTOINCREMENT
                + KEY_ARTICLEID + " INTEGER NOT NULL,"
                + KEY_ARTICLETITLE + " TEXT NOT NULL,"
                + KEY_ARTICLEDATE + " TEXT NOT NULL,"
                + KEY_ARTICLENAME + " TEXT NOT NULL,"
                + KEY_ARTICLECLASS + " TEXT NOT NULL,"
                + KEY_ARTICLECONTENT + " TEXT NOT NULL,"
                + KEY_ARTICLETYPE + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_ARTICLE_TABLE);

        String CREATE_ASSIGNMENT_TABLE = "CREATE TABLE " + TABLE_ASSIGNMENT + "(" //PRIMARY KEY AUTOINCREMENT
                + KEY_ASSIGNMENTID + " INTEGER NOT NULL,"
                + KEY_ASSIGNMENTSUBJECT + " TEXT NOT NULL,"
                + KEY_ASSIGNMENTTITLE + " TEXT NOT NULL,"
                + KEY_ASSIGNMENTDETAIL + " TEXT NOT NULL,"
                + KEY_ASSIGNMENTSUBM + " TEXT NOT NULL,"
                + KEY_TEACHER + " TEXT NOT NULL,"
                + KEY_ASSIGNMENTCLASS + " TEXT NOT NULL,"
                + KEY_ASSIGNMENTEXTRA + " TEXT NOT NULL" + ")";
        db.execSQL(CREATE_ASSIGNMENT_TABLE);
        
        this.addData(db); //add news data
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        // Create tables again
        onCreate(db);
    }
    
    public void addData(SQLiteDatabase db) {
    	//Call functions to populate each database table seperately
    	addQuizData(db);
    	//Toast.makeText(cont, "All values inserted", Toast.LENGTH_SHORT).show();
        //db.close(); // Closing database connection
    }

    public void addQuizData(SQLiteDatabase db) {
        //Add default records to animals
        ContentValues _Values = new ContentValues();
        //Get xml resource file
        android.content.res.Resources res = cont.getResources();

        //Open xml file
        XmlResourceParser _xml = res.getXml(R.xml.quizq);
        try {
            //Check for end of document
            int eventType = _xml.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //Search for record tags
                if ((eventType == XmlPullParser.START_TAG) && (_xml.getName().equals("record"))) {
                    //Record tag found, now get values and insert record
                    String _subject = _xml.getAttributeValue(null, FI_SUB);
                    String _question = _xml.getAttributeValue(null, FI_QUESTION);
                    String _option1 = _xml.getAttributeValue(null, FI_OPT1);
                    String _option2 = _xml.getAttributeValue(null, FI_OPT2);
                    String _option3 = _xml.getAttributeValue(null, FI_OPT3);
                    String _option4 = _xml.getAttributeValue(null, FI_OPT4);
                    String _ans = _xml.getAttributeValue(null, FI_QANS);
                    String _diff = _xml.getAttributeValue(null, FI_DIFF);

                    //_Values.put(KEY_QSUB, _subject);
                    _Values.put(KEY_Q, _question);
                    _Values.put(KEY_QOPTION1, _option1);
                    _Values.put(KEY_QOPTION2, _option2);
                    _Values.put(KEY_QOPTION3, _option3);
                    _Values.put(KEY_QOPTION4, _option4);
                    _Values.put(KEY_QANS, _ans);
                    //_Values.put(KEY_DIFF, _diff);

                    db.insert(TABLE_QUIZ, null, _Values);
                }
                eventType = _xml.next();
            }
        }
        //Catch errors
        catch (XmlPullParserException e) {
            Log.e("SQLITE", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("SQLITE", e.getMessage(), e);

        } finally {
            //Close the xml file
            _xml.close();
        }
       // db.close();

    }

    public Boolean addQuizDataOnline(String quest) {
        //Addquiz data to

        ContentValues _Values = new ContentValues();

        String [] questions = quest.split("\r\n");

        for(int i = 0; i < questions.length; i++) {

                String[] indQuestn = questions[i].split("::");

                //String _subject = _xml.getAttributeValue(null, FI_SUB);
                String _question = indQuestn[0];
                String _option1 = indQuestn[1];
                String _option2 = indQuestn[2];
                String _option3 = indQuestn[3];
                String _option4 = indQuestn[4];
                String _ans = indQuestn[5];
                //String _diff = _xml.getAttributeValue(null, FI_DIFF);

            // Storing JSON item in a Variable

            //_Values.put(KEY_QSUB, _subject);
            _Values.put(KEY_Q, _question);
            _Values.put(KEY_QOPTION1, _option1);
            _Values.put(KEY_QOPTION2, _option2);
            _Values.put(KEY_QOPTION3, _option3);
            _Values.put(KEY_QOPTION4, _option4);
            _Values.put(KEY_QANS, _ans);

            //_Values.put(KEY_DIFF, _diff);

            db.insert(TABLE_QUIZ, null, _Values);

        }//end forloop

        //db.close();
        return true;
    }


    public Boolean addNewsData(JSONArray response) {
        //Add default records to animals
        ContentValues _Values = new ContentValues();

        for(int i = 0; i < response.length(); i++) {
            JSONObject c = null;
            String id = null, date= null, title= null, detail= null, type= null;
            try {
                c = response.getJSONObject(i);

                 id = c.getString("newsid");
                 date = c.getString("news_date");
                 title = c.getString("news_header");
                 detail = c.getString("news_detail");
                 type = c.getString("news_type");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Storing JSON item in a Variable

                    _Values.put(KEY_NEWSID, id);
                    _Values.put(KEY_NEWSTITLE, title);
                    _Values.put(KEY_NEWSDETAIL, detail);
                    _Values.put(KEY_NEWSTYPE, type);
                    _Values.put(KEY_NEWSDATE, date);

                    db.insert(TABLE_NEWS, null, _Values);
            }//end forloop

            //db.close();
        return true;
        }

    public Boolean addEventData(JSONArray response) {
        //Add default records to animals
        ContentValues _Values = new ContentValues();

        for(int i = 0; i < response.length(); i++) {
            JSONObject c = null;
            String id = null, date= null, header= null, detail= null, participant= null;
            try {
                c = response.getJSONObject(i);

                id = c.getString("event_id");
                participant = c.getString("participants");
                header = c.getString("title");
                detail = c.getString("detail");

                date = c.getString("event_date");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Storing JSON item in a Variable
            _Values.put(KEY_EVENTID, id);
            _Values.put(KEY_EVENTHEADER, header);
            _Values.put(KEY_EVENTDETAIL, detail);
            _Values.put(KEY_EVENTPARTICIPANT, participant);
            _Values.put(KEY_EVENTDATE, date);

            db.insert(TABLE_EVENTS, null, _Values);
        }//end forloop
        return true;
        //db.close();
    }

    public Boolean addCalendarData(JSONArray response) {
        //Add records to calendar table
        ContentValues _Values = new ContentValues();

        for(int i = 0; i < response.length(); i++) {
            JSONObject c = null;
            String id = null, date= null, pictitle= null, header= null, detail= null, interval= null;
            try {
                c = response.getJSONObject(i);

                id = c.getString("calendarid");
                date = c.getString("pic_monthdate");
                pictitle = c.getString("pic_title");
                header = c.getString("title");
                detail = c.getString("details");
                interval = c.getString("timeframe");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Storing JSON item in a Variable
            _Values.put(KEY_CALENID, id);
            _Values.put(KEY_CALENDAYMONTH, date);
            _Values.put(KEY_CALENPICTITLE, pictitle);
            _Values.put(KEY_CALENHEADER, header);
            _Values.put(KEY_CALENDETAIL, detail);
            _Values.put(KEY_CALENINTERVAL, interval);

            db.insert(TABLE_CALENDAR, null, _Values);
        }//end forloop
        return true;
        //db.close();
    }


    public Boolean addArticleData(JSONArray response) {
        //Add records to calendar table
        ContentValues _Values = new ContentValues();

        for(int i = 0; i < response.length(); i++) {
            JSONObject c = null;
            String id = null, date= null, title= null, stname= null, stclass= null, artcontent= null, arttype= null;
            try {
                c = response.getJSONObject(i);

                id = c.getString("article_id");
                date = c.getString("date_added");
                title = c.getString("title");
                stname = c.getString("added_by");
                stclass = c.getString("stud_class");

                artcontent = c.getString("content");
                arttype = c.getString("type");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Storing JSON item in a Variable
            _Values.put(KEY_ARTICLEID, id);
            _Values.put(KEY_ARTICLEDATE, date);
            _Values.put(KEY_ARTICLETITLE, title);
            _Values.put(KEY_ARTICLENAME, stname);
            _Values.put(KEY_ARTICLECLASS, stclass);
            _Values.put(KEY_ARTICLECONTENT, artcontent);
            _Values.put(KEY_ARTICLETYPE, arttype);

            db.insert(TABLE_ARTICLE, null, _Values);
        }//end forloop
        return true;
        //db.close();
    }


    public Boolean addAssignmentData(JSONArray response) {
        //Add default records to animals
        ContentValues _Values = new ContentValues();


        for(int i = 0; i < response.length(); i++) {
            JSONObject c = null;
            String id = null, subjtitle= null, questitle= null, questdetail= null, questsubmdate= null, questteacher= null, questclass= null, questextra= null;
            try {
                c = response.getJSONObject(i);

                id = c.getString("assign_id");
                subjtitle = c.getString("subject");
                questitle = "All Subjects";//c.getString("questitle");
                questdetail = c.getString("question");
                questsubmdate = c.getString("subm_date");
                questteacher = c.getString("teacher_id");
                questclass = c.getString("class");
                questextra = c.getString("extra_info");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            _Values.put(KEY_ASSIGNMENTID, id);
            _Values.put(KEY_ASSIGNMENTSUBJECT, subjtitle);
            _Values.put(KEY_ASSIGNMENTTITLE, questitle);
            _Values.put(KEY_ASSIGNMENTDETAIL, questdetail);
            _Values.put(KEY_ASSIGNMENTSUBM, questsubmdate);
            _Values.put(KEY_TEACHER, questteacher);
            _Values.put(KEY_ASSIGNMENTCLASS, questclass);
            _Values.put(KEY_ASSIGNMENTEXTRA, questextra);

            db.insert(TABLE_ASSIGNMENT, null, _Values);
        }//end forloop
        return true;
        //db.close();
    }

    public String[][] getQuizData(){ //passed in subject and difficulty
    	String selectQuery = "";
    	String res = "";

    		//selectQuery = "SELECT  * FROM " + TABLE_QUIZ + " WHERE subject= '" + subject + "' AND difficulty= '"+dif+"' ORDER BY RANDOM() LIMIT 15";//
            selectQuery = "SELECT  * FROM " + TABLE_QUIZ + " ORDER BY RANDOM() LIMIT 15";//

            rows = new String[14][6]; //14 rows returned, 1 question, 4 options and 1 answer == 6
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);

            int count = 0;
	        // Move to first row
	        if(cursor.moveToFirst()) {
                do {
                    rows[count][0] = "" + cursor.getString(cursor.getColumnIndex(KEY_Q)); //Question
                    rows[count][1] = cursor.getString(cursor.getColumnIndex(KEY_QOPTION1)); //Option1
                    rows[count][2] = cursor.getString(cursor.getColumnIndex(KEY_QOPTION2)); //Option2
                    rows[count][3] = cursor.getString(cursor.getColumnIndex(KEY_QOPTION3)); //Option3
                    rows[count][4] = cursor.getString(cursor.getColumnIndex(KEY_QOPTION4)); //Option4
                    rows[count][5] = cursor.getString(cursor.getColumnIndex(KEY_QANS)); //Answer
	        		/*if(!cursor.isLast())
	        			res += cursor.getString(cursor.getColumnIndex(KEY_Q))+ ":::" +cursor.getString(cursor.getColumnIndex(KEY_QOPTIONS))+ ":::" +cursor.getString(cursor.getColumnIndex(KEY_QANS)) + ":::";
	        		else
	        			res += cursor.getString(cursor.getColumnIndex(KEY_Q))+ ":::" +cursor.getString(cursor.getColumnIndex(KEY_QOPTIONS))+ ":::" +cursor.getString(cursor.getColumnIndex(KEY_QANS));*/
                   /* Log.e("DB QUESTiONS", "QSQ >> " + cursor.getString(cursor.getColumnIndex(KEY_Q)));
                    Log.e("DB QUESTiONS", "QS >> " + rows[count][0]);*/
                    count++;
                    //Toast.makeText(cont, "Quiz from db"+cursor.getString(cursor.getColumnIndex(KEY_Q)), Toast.LENGTH_SHORT).show();
                } while (cursor.moveToNext() && count < 14);

            }
            /*Log.e("DB QUESTiONS", "QSER >> "+rows.toString());
            Log.e("DB QUESTiONS", "QSER Row 9 >> "+rows[9][0].toString());*/
            cursor.close();
            db.close();
            //Log.e("DB QUESTiONS", "QSE >> "+rows);
            //Toast.makeText(cont, "Quiz from db ROWS"+rows.toString(), Toast.LENGTH_LONG).show();
			return rows;
    	}


    public Cursor getNewsData(){ //passed
        String selectQuery = "";
        String res = "";

            selectQuery = "SELECT  * FROM " + TABLE_NEWS + " Order by "+ KEY_NEWSID + " ASC";//

        //String[][] rows = new String[][]; //rows returned
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            return cursor;
    }

    public Cursor getEventData(){ //passed
        String selectQuery = "SELECT * FROM " + TABLE_EVENTS + " Order by "+ KEY_EVENTID + " ASC";//

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getCalendarData(){ //passed
        String selectQuery = "SELECT * FROM " + TABLE_CALENDAR + "";//

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getArticleData(){ //passed
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLE + "";//

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getAssignmentData(){ //passed
        String selectQuery = "SELECT * FROM " + TABLE_ASSIGNMENT ;//+ " WHERE assignment_class='"+stclass+"'";//

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }

    public Boolean clearData(int s){ //passed
        String selectQuery = "";

        if(s == 1)
            selectQuery = "DELETE FROM " + TABLE_NEWS + "";//WHERE newsid <> -1
        else if(s==2)
            selectQuery = "DELETE FROM " + TABLE_EVENTS + "";//
        else if(s==3)
            selectQuery = "DELETE FROM " + TABLE_CALENDAR + "";//
        else if(s==4)
            selectQuery = "DELETE FROM " + TABLE_ASSIGNMENT + "";//
        else if(s==5)
            selectQuery = "DELETE FROM " + TABLE_ARTICLE + "";//

        //String[][] rows = new String[][]; //rows returned
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(selectQuery);
        return true;
    }

    /*public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_JSCRIPT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }*/
   
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_QUIZ, null, null);
        db.close();
    }
    
    /*public ContentValues getJscript(){
    	
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, "Overview"); // Title
        values.put(KEY_CONTENT, "Javascript is a very useful tool for both front end and back end rubbish"); // Content
        
        values.put(KEY_TITLE, "Challenge Javascript"); // Title
        values.put(KEY_CONTENT,"Object oriented Javascript");
    	return values;	
    }
    
    public ContentValues getJava(){
    	 ContentValues values = new ContentValues();
         values.put(KEY_TITLE, "Overview Java"); // Title
         values.put(KEY_CONTENT, "Java is a very useful networking tool...cant really see it's use tho"); // Content
         
         values.put(KEY_TITLE, "Encapsulation in Java"); // Title
         values.put(KEY_CONTENT,"Encapsulation is a procedure which helps protect data from being accessed by unauthorized external functions");
     	return values;	
    }
    
    public ContentValues getPython(){
    	 ContentValues values = new ContentValues();
         values.put(KEY_TITLE, "Overview"); // Title
         values.put(KEY_CONTENT, "Python is a very useful tool for both front end and back end rubbish"); // Content
         
         values.put(KEY_TITLE, "Inheritance"); // Title
         values.put(KEY_CONTENT,"One stop shot for...");
     	return values;	
    }
    
    public ContentValues getCsharp(){
    	 ContentValues values = new ContentValues();
         values.put(KEY_TITLE, "Overview"); // Title
         values.put(KEY_CONTENT, "C# is a very useful tool for both front end and back end rubbish"); // Content
         
         values.put(KEY_TITLE, "Data Protections"); // Title
         values.put(KEY_CONTENT,"Protection in C# is done in the form of...");
     	return values;	
    }
    
    public ContentValues getFaq(){
    	 ContentValues values = new ContentValues();
    	 values.put(KEY_FAQ_LANG, "Javascript"); // Title
         values.put(KEY_FAQ_TITLE, "FAQ1 : How useful is Javascript?"); // Title
         values.put(KEY_FAQ_ANSWER, "Javascript is a very useful tool for botyh front end and back end rubbish"); // Content
         
         values.put(KEY_FAQ_LANG, "Java"); // Title
         values.put(KEY_FAQ_TITLE, "FAQ1 : What is the importance of Java?"); // Title
         values.put(KEY_FAQ_ANSWER,"Java is a very important language");
     	return values;	
    }*/
}