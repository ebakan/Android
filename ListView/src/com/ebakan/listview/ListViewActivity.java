package com.ebakan.listview;


import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.ExpandableListActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ListViewActivity extends ExpandableListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
  	  //setExpandableListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, ITEMS));
  	      setListAdapter(new CustomListAdapter(getAssets()));
    }
    
    public class CustomListAdapter extends BaseExpandableListAdapter {
    	private String[] titles;
    	private String[][] elems;
    	public CustomListAdapter(AssetManager manager) {
    		super();
    		
    		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = null;
    		Document doc = null;
    		try {
    		    docBuilder = docBuilderFactory.newDocumentBuilder();
    		}
    		catch (ParserConfigurationException e) {
    		    return;
    		}

    		try {
    			doc = docBuilder.parse(manager.open("links.xml"));
			} catch (IOException e) {
				return;
			} catch (SAXException e) {
				return;
			}
			
			Element root=doc.getDocumentElement();
			NodeList elements=root.getElementsByTagName("element");
			titles = new String[elements.getLength()];
			elems = new String[elements.getLength()][0];
			for(int i=0;i<elements.getLength();i++) {
				Element element = (Element)elements.item(i);
				titles[i] = element.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
				String link = element.getElementsByTagName("link").item(0).getFirstChild().getNodeValue();
				elems[i] = new String[1];
				elems[i][0] = link;
				try {
	    			doc = docBuilder.parse((new URL(link)).openStream());
				} catch (IOException e) {
					return;
				} catch (SAXException e) {
					return;
				}
				
				root=doc.getDocumentElement();
				NodeList items=root.getElementsByTagName("item");
				
				elems[i] = new String[items.getLength()-1];
				for(int j=0;j<items.getLength()-1;j++) {
					Element item = (Element)items.item(j+1);
					elems[i][j] = item.getElementsByTagName("title").item(0).getFirstChild().getNodeValue();
					//System.out.println(item.getElementsByTagName("title").item(0).getFirstChild().getNodeValue());
				}
				
			}
    	}
    	public Object getChild(int groupPosition, int childPosition) {
            return elems[groupPosition][childPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return elems[groupPosition].length;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                View convertView, ViewGroup parent) {
        	AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        	TextView textView = new TextView(ListViewActivity.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(20, 10, 20, 10);
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }

        public Object getGroup(int groupPosition) {
            return titles[groupPosition];
        }

        public int getGroupCount() {
            return titles.length;
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                ViewGroup parent) {
        	AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 64);

            TextView textView = new TextView(ListViewActivity.this);
            textView.setLayoutParams(lp);
            // Center the text vertically
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            textView.setPadding(60, 0, 0, 0);
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }
    }
}