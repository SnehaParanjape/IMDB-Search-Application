package GUI;

import java.sql.*;
import java.util.ArrayList;
import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;

import javax.swing.JTextArea;

public class hw3
{
	private JFrame frmMovieDatabaseSearch;
	static Connection con = null;
	static ResultSet result = null;
	static PreparedStatement prepStatement = null;
	static PreparedStatement prepStatement2 = null;
	
	private JList<String> genreJList, countryJList, movieQJList,movieDQJList, userQJList, actorJList, directorJList, tagsValueJList; 
	
	DefaultListModel genreModel = new DefaultListModel();
	DefaultListModel countryModel = new DefaultListModel();
	DefaultListModel actorModel = new DefaultListModel();
	DefaultListModel directorModel = new DefaultListModel();
	DefaultListModel tagModel = new DefaultListModel();
	DefaultListModel movieModel = new DefaultListModel();
	DefaultListModel movieDetailModel = new DefaultListModel();
	DefaultListModel userModel = new DefaultListModel();
		
	ArrayList<String> genreList = new ArrayList();
	ArrayList<String> countryList = new ArrayList();
	ArrayList<String> actorList = new ArrayList();
	ArrayList<String> directorList = new ArrayList();
	ArrayList<String> tagList = new ArrayList();
	ArrayList<String> tagList_1 = new ArrayList();
	ArrayList<String> movieList = new ArrayList();
	ArrayList<String> userList = new ArrayList();
	
	private String start = "", stop = "", movie="", movieC="", value = "", country = "", tags="", resultQuery = "", resultQuery2 = "";
	private JTextField to_textField;
	private JTextField from_textField;
	private JComboBox and_or_comboBox, tag_wgt_comboBox;
	private JTextField value_textField;

	/*Launch the application*/
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					hw3 window = new hw3();
					window.frmMovieDatabaseSearch.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/*Create the application*/
	public hw3()
	{
		initialize();
		run();
	}

	/*Initialize the contents of the frame*/
	
	public void run()
	{
		try
		{
			con = openConnection();
			populateGenre();
		}
		catch (SQLException e)
		{
			System.err.println("Error occured when communicating with the database server: " + e.getMessage());
		}
		catch (ClassNotFoundException e)
		{
			System.err.println("Cannot find the database driver");
		}
		finally
		{
			
		}
	}
	
	private void initialize()
	{
		frmMovieDatabaseSearch = new JFrame();
		frmMovieDatabaseSearch.setTitle("Movie Search Database");
		frmMovieDatabaseSearch.getContentPane().setBackground(SystemColor.menu);
		frmMovieDatabaseSearch.setBounds(100, 100, 1075, 743);
		frmMovieDatabaseSearch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMovieDatabaseSearch.getContentPane().setLayout(null);
		
		//Movie Results Label Panel
		JPanel panel_movie_results_label = new JPanel();
		panel_movie_results_label.setBackground(Color.LIGHT_GRAY);
		panel_movie_results_label.setBounds(599, 0, 460, 38);
		frmMovieDatabaseSearch.getContentPane().add(panel_movie_results_label);
		panel_movie_results_label.setLayout(null);
		
		//Movie Results Label
		JLabel movie_results_label = new JLabel("Movie Results");
		movie_results_label.setFont(new Font("Times New Roman", Font.BOLD, 20));
		movie_results_label.setBackground(SystemColor.activeCaption);
		movie_results_label.setHorizontalAlignment(SwingConstants.CENTER);
		movie_results_label.setBounds(109, 0, 227, 33);
		panel_movie_results_label.add(movie_results_label);
		
		//Genres Label Panel
		JPanel panel_genres_label = new JPanel();
		panel_genres_label.setBackground(Color.LIGHT_GRAY);
		panel_genres_label.setBounds(0, 0, 141, 38);
		frmMovieDatabaseSearch.getContentPane().add(panel_genres_label);
		panel_genres_label.setLayout(null);
		
		//Genres Label
		JLabel genres_label = new JLabel("Genres");
		genres_label.setBounds(0, 0, 141, 36);
		panel_genres_label.add(genres_label);
		genres_label.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		genres_label.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Genre List Scroll Pane
		JScrollPane genre_scrollPane = new JScrollPane();
		genre_scrollPane.setBounds(0, 38, 141, 185);
		frmMovieDatabaseSearch.getContentPane().add(genre_scrollPane);
		
		genreJList = new JList<>();
		genreJList.setBackground(SystemColor.menu);
		genreJList.setBounds(0, 184, 139, -184);
		
		//Genre List Panel
		JPanel genrePanel = new JPanel();
		genre_scrollPane.setViewportView(genrePanel);
		genrePanel.setBackground(new Color(240, 240, 240));
		genrePanel.setForeground(new Color(0, 128, 128));
		genrePanel.add(genreJList);
		
		//Movie Year Panel
		JPanel movie_year_panel = new JPanel();
		movie_year_panel.setBackground(Color.LIGHT_GRAY);
		movie_year_panel.setBounds(-17, 223, 158, 30);
		frmMovieDatabaseSearch.getContentPane().add(movie_year_panel);
		movie_year_panel.setLayout(null);
		
		//Movie Year Label		
		JLabel movie_year_label = new JLabel("Movie Year");
		movie_year_label.setBounds(10, 0, 135, 27);
		movie_year_panel.add(movie_year_label);
		movie_year_label.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		movie_year_label.setHorizontalAlignment(SwingConstants.CENTER);
		
		//From & To panel
		JPanel from_to_panel = new JPanel();
		from_to_panel.setBounds(0, 251, 141, 71);
		frmMovieDatabaseSearch.getContentPane().add(from_to_panel);
		from_to_panel.setLayout(null);
		
		//From label
		JLabel from_label = new JLabel("From");
		from_label.setHorizontalAlignment(SwingConstants.CENTER);
		from_label.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		from_label.setBounds(0, 3, 56, 29);
		from_to_panel.add(from_label);
		
		//To label
		JLabel to_label = new JLabel("To");
		to_label.setHorizontalAlignment(SwingConstants.CENTER);
		to_label.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		to_label.setBounds(0, 43, 56, 23);
		from_to_panel.add(to_label);
		
		//To textField
		to_textField = new JTextField();
		to_textField.setBounds(52, 6, 79, 23);
		from_to_panel.add(to_textField);
		to_textField.setColumns(10);
		to_textField.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
	            start=to_textField.getText();
	        }
	        public void keyReleased( KeyEvent e)
	        {
	        	start = to_textField.getText();
	        }
	        public void keyTyped( KeyEvent e)
	        {
	        	start = to_textField.getText();
	        }
	    });
		
		//From TextField
		from_textField = new JTextField();
		from_textField.setBounds(52, 43, 79, 22);
		from_to_panel.add(from_textField);
		from_textField.setColumns(10);
		from_textField.addKeyListener(new KeyListener()
		{
	        public void keyPressed(KeyEvent e)
	        {
	            stop=from_textField.getText();
	        }
	        public void keyReleased( KeyEvent e)
	        {
	        	stop = from_textField.getText();
	        }
	        public void keyTyped( KeyEvent e)
	        {
	        	stop = from_textField.getText();
	        }
	    });
		
		//Country label panel
		JPanel country_label_panel = new JPanel();
		country_label_panel.setBackground(Color.LIGHT_GRAY);
		country_label_panel.setBounds(142, 0, 141, 38);
		frmMovieDatabaseSearch.getContentPane().add(country_label_panel);
		country_label_panel.setLayout(null);
		
		//Country label
		JLabel country_label = new JLabel("Country");
		country_label.setBounds(0, 0, 141, 33);
		country_label_panel.add(country_label);
		country_label.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		country_label.setHorizontalAlignment(SwingConstants.CENTER);
		
		//And/Or ComboBox panel
		JPanel and_or_panel = new JPanel();
		and_or_panel.setBackground(Color.LIGHT_GRAY);
		and_or_panel.setBounds(0, 365, 440, 64);
		frmMovieDatabaseSearch.getContentPane().add(and_or_panel);
		and_or_panel.setLayout(null);
		
		//And/Or ComboBox
		and_or_comboBox = new JComboBox();
		and_or_comboBox.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		and_or_comboBox.setBounds(60, 35, 284, 23);
		and_or_comboBox.setMaximumRowCount(3);
		and_or_comboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select AND, OR ", "AND", "OR" }));
		and_or_panel.add(and_or_comboBox);
		
		//Search Attributes' Values Label
		JLabel search_attr_values_label = new JLabel("Search Between \nAttributes' Values:");
		search_attr_values_label.setForeground(Color.DARK_GRAY);
		search_attr_values_label.setBounds(21, 0, 360, 35);
		and_or_panel.add(search_attr_values_label);
		search_attr_values_label.setFont(new Font("Times New Roman", Font.BOLD, 20));
		search_attr_values_label.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Country Scroll Pane
		JScrollPane country_scrollPane = new JScrollPane();
		country_scrollPane.setBounds(142, 38, 141, 284);
		frmMovieDatabaseSearch.getContentPane().add(country_scrollPane);
		
		countryJList = new JList();
		countryJList.setBackground(SystemColor.menu);
		
		//Country list panel
		JPanel country_list_panel = new JPanel();
		country_scrollPane.setViewportView(country_list_panel);
		country_list_panel.add(countryJList);
		
		//Cast label panel
		JPanel cast_label_panel = new JPanel();
		cast_label_panel.setLayout(null);
		cast_label_panel.setBackground(Color.LIGHT_GRAY);
		cast_label_panel.setBounds(284, 0, 141, 38);
		frmMovieDatabaseSearch.getContentPane().add(cast_label_panel);
		
		//Cast label
		JLabel lblCast = new JLabel("Cast");
		lblCast.setBounds(0, 0, 147, 32);
		cast_label_panel.add(lblCast);
		lblCast.setHorizontalAlignment(SwingConstants.CENTER);
		lblCast.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		
		//Tag Ids and Values label panel
		JPanel tags_label_panel = new JPanel();
		tags_label_panel.setLayout(null);
		tags_label_panel.setBackground(Color.LIGHT_GRAY);
		tags_label_panel.setBounds(426, 0, 174, 38);
		frmMovieDatabaseSearch.getContentPane().add(tags_label_panel);
		
		//Tag Ids and Values label
		JLabel lblTagIdsValues = new JLabel("Tag IDs and Values");
		lblTagIdsValues.setBounds(0, 0, 151, 33);
		tags_label_panel.add(lblTagIdsValues);
		lblTagIdsValues.setHorizontalAlignment(SwingConstants.CENTER);
		lblTagIdsValues.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		
		//Tag Ids and Values Scroll Pane
		JScrollPane tags_scrollPane = new JScrollPane();
		tags_scrollPane.setBounds(430, 38, 190, 200);
		frmMovieDatabaseSearch.getContentPane().add(tags_scrollPane);
				
		//Tags List Panel
		JPanel tags_list_panel = new JPanel();
		tags_scrollPane.setViewportView(tags_list_panel);
				
		tagsValueJList = new JList();
		tags_list_panel.add(tagsValueJList);
		
		//Movie Results Scroll Pane
		JScrollPane movie_scrollPane = new JScrollPane();
		movie_scrollPane.setBounds(625, 38, 420, 100);
		frmMovieDatabaseSearch.getContentPane().add(movie_scrollPane);
		
		//Movie List Panel
		JPanel movie_list_panel = new JPanel();
		movie_scrollPane.setViewportView(movie_list_panel);
		
		movieQJList = new JList();
		movie_list_panel.add(movieQJList);
		
		//Movie Detail Results Scroll Pane
		JScrollPane movie_details_scrollPane = new JScrollPane();
		movie_details_scrollPane.setBounds(625, 140, 420, 190);
		frmMovieDatabaseSearch.getContentPane().add(movie_details_scrollPane);
				
		//Movie Detail List Panel
		JPanel movie_details_list_panel = new JPanel();
		movie_details_scrollPane.setViewportView(movie_details_list_panel);
				
		movieDQJList = new JList();
		movie_details_list_panel.add(movieDQJList);
		
		//Tag Weight & Value panel
		JPanel tag_wgt_value_panel = new JPanel();
		tag_wgt_value_panel.setBounds(425, 240, 208, 79);
		frmMovieDatabaseSearch.getContentPane().add(tag_wgt_value_panel);
		tag_wgt_value_panel.setLayout(null);
		
		//Tag Weight Label
		JLabel lblTagWeight = new JLabel("Tag Weight");
		lblTagWeight.setBounds(0, 19, 55, 23);
		lblTagWeight.setHorizontalAlignment(SwingConstants.CENTER);
		lblTagWeight.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		tag_wgt_value_panel.add(lblTagWeight);
		
		//Value Label
		JLabel lblValue = new JLabel("Value");
		lblValue.setHorizontalAlignment(SwingConstants.CENTER);
		lblValue.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		lblValue.setBounds(0, 53, 55, 23);
		tag_wgt_value_panel.add(lblValue);
		
		//Tag Weight ComboBox
		tag_wgt_comboBox = new JComboBox();
		tag_wgt_comboBox.setBounds(79, 18, 98, 24);
		tag_wgt_comboBox.setMaximumRowCount(3);
		tag_wgt_comboBox.setModel(new DefaultComboBoxModel(new String[] {"=", ">", "<"}));
		tag_wgt_value_panel.add(tag_wgt_comboBox);
		
		//Value TextField
		value_textField = new JTextField();
		value_textField.setBounds(79, 53, 98, 23);
		tag_wgt_value_panel.add(value_textField);
		value_textField.setColumns(10);
		value_textField.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
				value=value_textField.getText();
		    }
		    public void keyReleased( KeyEvent e)
		    {
		    	value = value_textField.getText();
		    }
		    public void keyTyped( KeyEvent e)
		    {
		    	value = value_textField.getText();
		       	System.out.println(value);
		    }
	    });
		
		//Query TextArea scrollPane
		JScrollPane query_scrollPane = new JScrollPane();
		query_scrollPane.setBounds(12, 440, 431, 185);
		frmMovieDatabaseSearch.getContentPane().add(query_scrollPane);
		
		//Query Results TextArea
		JTextArea query_results_textArea = new JTextArea();
		query_scrollPane.setViewportView(query_results_textArea);
		
		//User Results Panel
		JPanel user_results_panel = new JPanel();
		user_results_panel.setLayout(null);
		user_results_panel.setBackground(SystemColor.activeCaptionBorder);
		user_results_panel.setBounds(447, 365, 602, 39);
		frmMovieDatabaseSearch.getContentPane().add(user_results_panel);
		
		//User Results Label
		JLabel lblUserResults = new JLabel("User Results");
		lblUserResults.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserResults.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblUserResults.setBackground(SystemColor.activeCaption);
		lblUserResults.setBounds(206, 0, 200, 39);
		user_results_panel.add(lblUserResults);
		
		//User Results ScrollPane
		JScrollPane user_results_scrollPane = new JScrollPane();
		user_results_scrollPane.setBounds(447, 400, 602, 265);
		frmMovieDatabaseSearch.getContentPane().add(user_results_scrollPane);
		
		//User Results List Panel
		JPanel user_results_list_panel = new JPanel();
		user_results_scrollPane.setViewportView(user_results_list_panel);
		
		userQJList = new JList();
		user_results_list_panel.add(userQJList);
		
		//Execute Query Button
		JButton execute_query_btn = new JButton("Execute Query");
		execute_query_btn.setBackground(new Color(64, 200, 208));
		execute_query_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				populateMovieSearch(evt);
			}
		});
		execute_query_btn.setBounds(120, 630, 180, 38);
		frmMovieDatabaseSearch.getContentPane().add(execute_query_btn);

		//Actor Label
		JLabel lblActor = new JLabel("Actor / Actress");
		lblActor.setBounds(300, 30, 130, 32);
		frmMovieDatabaseSearch.getContentPane().add(lblActor);
		lblActor.setFont(new Font("Times New Roman", Font.PLAIN, 15));
				
		//Actor ScrollPane
		JScrollPane actor_scrollPane = new JScrollPane();
		actor_scrollPane.setBounds(285, 55, 141, 183);
		frmMovieDatabaseSearch.getContentPane().add(actor_scrollPane);
				
		//Actor Panel
		JPanel actor_panel = new JPanel();
		actor_scrollPane.setViewportView(actor_panel);
				
		//Actor List
		actorJList = new JList();
		actorJList.setBackground(SystemColor.menu);
		actor_panel.add(actorJList);
		
		//Director Label
		JLabel lblDirector = new JLabel("Director");
		lblDirector.setBounds(300, 235, 130, 32);
		frmMovieDatabaseSearch.getContentPane().add(lblDirector);
		lblDirector.setFont(new Font("Times New Roman", Font.PLAIN, 15));
		
		//Director ScrollPane
		JScrollPane director_scrollPane = new JScrollPane();
		director_scrollPane.setBounds(284, 260, 141, 61);
		frmMovieDatabaseSearch.getContentPane().add(director_scrollPane);
				
		//Director Panel
		JPanel director_panel = new JPanel();
		director_scrollPane.setViewportView(director_panel);
				
		//Director List
		directorJList = new JList();
		directorJList.setBackground(SystemColor.menu);
		director_panel.add(directorJList);
		
		//Find Country Button
		JButton btnFindCountry = new JButton("Find Country");
		btnFindCountry.setBounds(22, 333, 119, 23);
		frmMovieDatabaseSearch.getContentPane().add(btnFindCountry);
		btnFindCountry.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				populateCountry(evt);
				query_results_textArea.setText(resultQuery);
			}
		});
		
		//Find Actor Button
		JButton btnFindActor = new JButton("Find Actor");
		btnFindActor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				populateActor(evt);
				query_results_textArea.setText(resultQuery);
			}
		});
		btnFindActor.setBounds(180, 333, 100, 23);
		frmMovieDatabaseSearch.getContentPane().add(btnFindActor);
		
		//Find Director Button
		JButton btnFindDirector = new JButton("Find Director");
		btnFindDirector.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				populateDirector(evt);
				query_results_textArea.setText(resultQuery);
			}
		});
		btnFindDirector.setBounds(300, 333, 120, 23);
		frmMovieDatabaseSearch.getContentPane().add(btnFindDirector);
		
		//Find Tags Button
		JButton btnFindTags = new JButton("Find Tags");
		btnFindTags.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				populateTags(evt);
				query_results_textArea.setText(resultQuery);
			}
		});
		btnFindTags.setBounds(450, 333, 100, 23);
		frmMovieDatabaseSearch.getContentPane().add(btnFindTags);
		
		//Filter Tags button
		JButton btnFilterButton = new JButton("Filter tags");
		btnFilterButton.setBounds(560, 333, 100, 23);
		frmMovieDatabaseSearch.getContentPane().add(btnFilterButton);
		btnFilterButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				filterTags(evt);
				query_results_textArea.setText(resultQuery);
			}
		});
		
		//Movie Detail Button
		JButton movie_details_btn = new JButton("Movie Details");
		movie_details_btn.setBounds(700, 333, 130, 23);
		frmMovieDatabaseSearch.getContentPane().add(movie_details_btn);
		movie_details_btn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				populateMovieDetails(evt);
			}
		});
		
		//User Query Button
		JButton btnUserQuery = new JButton("Execute User Query");
		btnUserQuery.setBounds(875, 333, 150, 23);
		frmMovieDatabaseSearch.getContentPane().add(btnUserQuery);
		btnUserQuery.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent evt)
			{
				populateUserQuery(evt);
				query_results_textArea.setText(resultQuery);
			}
		});
		
	}
	private Connection openConnection() throws SQLException, ClassNotFoundException
	{
	    // Load the Oracle database driver 
	    DriverManager.registerDriver(new oracle.jdbc.OracleDriver()); 

	    /* 
	    Here is the information needed when connecting to a database 
	    server. These values are now hard-coded in the program. In 
	    general, they should be stored in some configuration file and 
	    read at run time. 
	    */ 
	    String host = "localhost"; 
	    String port = "1521";
	    String dbName = "sneha"; 
	    String userName = "scott"; 
	    String password = "tiger"; 

	    // Construct the JDBC URL 
	    String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + "/" + dbName; 
	    return DriverManager.getConnection(dbURL, userName, password); 
	}
	
	 /** 
     * Close the database connection 
     * @param con 
     */ 
    private void closeConnection(Connection con)
    { 
        try
        { 
            con.close(); 
        }
        catch (SQLException e)
        { 
            System.err.println("Cannot close connection: " + e.getMessage());
        } 
    }
    
    private void populateGenre()
    {
        String genre = "SELECT DISTINCT G.GENRE FROM MOVIE_GENRES G";
        try
        {
        	ResultSet rS = null;
            prepStatement=con.prepareStatement(genre);
            rS = prepStatement.executeQuery(genre);
            
            while(rS.next())
            {
                if(!genreModel.contains(rS.getString("genre")))
                {
                    genreModel.addElement(rS.getString("genre"));
                }
            }
            prepStatement.close();
            rS.close();
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
        
        genreJList.setModel(genreModel);
    
        MouseListener mouseListener = new MouseAdapter() 
        {
        	public void mouseClicked(MouseEvent e) 
        	{
        		if (e.getClickCount() == 1)
        		{
        			genreList = (ArrayList<String>) genreJList.getSelectedValuesList();
        		}
        	}
        };
        
        genreJList.addMouseListener(mouseListener);
    }
    	
    private void populateCountry(ActionEvent evt)
    {
    	country = "";
    	movie = "";
  		// TODO Auto-generated method stub
  		  
  		if(genreList.size()!=0)
  		{
  			String bAttr = "";
  	        
  			if(and_or_comboBox.getSelectedIndex() == 0 || and_or_comboBox.getSelectedIndex() == 1)
  			{
  				bAttr = "INTERSECT";
  			}
  			else
  			{
  				if(and_or_comboBox.getSelectedIndex() == 2)
  				{
  					bAttr = "UNION";
  				}
  			}
  			
  			int i=0;
  			if(start.length() == 4 && stop.length() == 4)
  			{
  				for(i=0;i<genreList.size()-1;i++)
  	  			{
  	  				movie += "SELECT DISTINCT M.MOVIEID\nFROM MOVIE_GENRES MG, MOVIES M, MOVIE_COUNTRIES MC\nWHERE M.MOVIEID=MG.MOVIEID AND M.MOVIEID=MC.MOVIEID AND M.MYEAR >='"+ start +"' AND M.MYEAR <='"+ stop +"' AND MG.GENRE = '"+genreList.get(i)+"'\n"+bAttr+"\n";
  	  			}

  				movie += "SELECT DISTINCT M.MOVIEID\nFROM MOVIE_GENRES MG, MOVIES M, MOVIE_COUNTRIES MC\nWHERE M.MOVIEID=MG.MOVIEID AND M.MOVIEID=MC.MOVIEID AND M.MYEAR >='"+ start +"' AND M.MYEAR <='"+ stop +"' AND MG.GENRE = '"+genreList.get(i)+"'";
  			}
  			else
  			{
  				for(i=0;i<genreList.size()-1;i++)
  	  			{
  	  				movie += "SELECT DISTINCT M.MOVIEID\nFROM MOVIE_GENRES MG, MOVIES M, MOVIE_COUNTRIES MC\nWHERE M.MOVIEID=MG.MOVIEID AND M.MOVIEID=MC.MOVIEID AND MG.GENRE = '"+genreList.get(i)+"'\n"+bAttr+"\n";
  	  			}

  				movie += "SELECT DISTINCT M.MOVIEID\nFROM MOVIE_GENRES MG, MOVIES M, MOVIE_COUNTRIES MC\nWHERE M.MOVIEID=MG.MOVIEID AND M.MOVIEID=MC.MOVIEID AND MG.GENRE = '"+genreList.get(i)+"'";
  			}
  			
  			country += "SELECT DISTINCT C.COUNTRY FROM MOVIE_COUNTRIES C \n WHERE C.MOVIEID IN ( " + movie +" ) \n";
  			
  			System.out.println(country);
  			  			
  			resultQuery = "SELECT DISTINCT MR.MOVIEID FROM MOVIES MR, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR \nWHERE MR.MOVIEID = MCR.MOVIEID AND MR.MOVIEID = MGR.MOVIEID AND MR.MOVIEID IN ( " + movie + " ) \n";
  			ResultSet rS = null;
  			ResultSet rs2 = null;
  			
  			countryModel.clear();
  			movieModel.clear();
  			
  			try
  			{
  				prepStatement = con.prepareStatement(country);
  				rS =prepStatement.executeQuery(country);

  				while(rS.next())
  				{   
  					if(!countryModel.contains(rS.getString("COUNTRY")))
  					{
  						countryModel.addElement(rS.getString("COUNTRY"));
  					}
  				}
  				prepStatement.close();
  				rS.close();
  			}
  			catch(Exception ex)
  			{
  				ex.printStackTrace();
  			}
  			countryJList.setModel(countryModel);
  	               
  			MouseListener mouseListener = new MouseAdapter() 
  			{
  				public void mouseClicked(MouseEvent e) 
  				{
  					if (e.getClickCount() == 1)
  					{
  						countryList = (ArrayList<String>) countryJList.getSelectedValuesList();
  						System.out.print(countryList);
  					}	
  				}
  			};
  			countryJList.addMouseListener(mouseListener);
  			
  			try
  			{
  				prepStatement2 = con.prepareStatement(resultQuery);
  				rs2 =prepStatement2.executeQuery(resultQuery);
  	               
  				movieModel.clear();
  				movieModel.addElement("ID");
  				while(rs2.next())
  				{
  	                    
  	  				movieModel.addElement(rs2.getString("MOVIEID"));

  				}
  				prepStatement2.close();
  				rs2.close();  
  			}
  			catch(Exception ex2)
  			{
  				ex2.printStackTrace();
  			}
  		movieQJList.setModel(movieModel);
  		}		
  	}
    
    private void populateActor(ActionEvent evt)
    {    	
    	String actor = "";
    	String bAttr = "";
    	movieC = "";
    		
    	if(and_or_comboBox.getSelectedIndex() == 0 || and_or_comboBox.getSelectedIndex() == 1)
    	{
    		bAttr = "INTERSECT";
    	}
    	else
    	{
    		if(and_or_comboBox.getSelectedIndex() == 2)
    		{
    			bAttr = "UNION";
    		}
    	}
    		  
    	if(countryList.size() == 0)
    	{
    		int start = 0;
    		int end = countryJList.getModel().getSize()-1;
    		countryJList.setSelectionInterval(start, end);
    		countryList = (ArrayList<String>) countryJList.getSelectedValuesList();	
    	}
    		  
    	//Genre Within attributes
    	int i = 0;
    			
    	for( i = 0; i < countryList.size() - 1; i++ )
    	{
    		actor += "SELECT DISTINCT MA.ACTORNAME \nFROM MOVIE_ACTORS MA, MOVIE_COUNTRIES MMC\nWHERE MA.MOVIEID= MMC.MOVIEID AND MMC.COUNTRY='"+countryList.get(i)+"' AND MA.MOVIEID IN("+movie+")"+"\n"+bAttr+"\n";
    		movieC += "SELECT DISTINCT MA.MOVIEID\nFROM MOVIE_ACTORS MA, MOVIE_COUNTRIES MMC\nWHERE MA.MOVIEID= MMC.MOVIEID AND MMC.COUNTRY='"+countryList.get(i)+"' AND MA.MOVIEID IN("+movie+")"+"\n"+bAttr+"\n";
    	}    
    			
    	actor += "SELECT DISTINCT MA.ACTORNAME\nFROM MOVIE_ACTORS MA, MOVIE_COUNTRIES MMC\nWHERE MA.MOVIEID= MMC.MOVIEID AND MMC.COUNTRY='"+countryList.get(i)+"' AND MA.MOVIEID IN("+movie+")"+"\n";
    	movieC += "SELECT DISTINCT MA.MOVIEID\nFROM MOVIE_ACTORS MA, MOVIE_COUNTRIES MMC\nWHERE MA.MOVIEID= MMC.MOVIEID AND MMC.COUNTRY='"+countryList.get(i)+"' AND MA.MOVIEID IN("+movie+")"+"\n";
    			
    	System.out.println(movieC);
    	
    	resultQuery = "";
    	    	
    	for( i = 0; i < countryList.size() - 1; i++ )
    	{  	
    		resultQuery = "SELECT DISTINCT MR.MOVIEID FROM MOVIES MR, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR \nWHERE MR.MOVIEID = MCR.MOVIEID AND MR.MOVIEID = MGR.MOVIEID AND MR.MOVIEID IN ( " + movieC + " ) \n";
    	}
    	
    	resultQuery = "SELECT DISTINCT MR.MOVIEID FROM MOVIES MR, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR \nWHERE MR.MOVIEID = MCR.MOVIEID AND MR.MOVIEID = MGR.MOVIEID AND MR.MOVIEID IN ( " + movieC + " ) \n";
    	
    	ResultSet rS = null;
    	actorModel.clear();
    	          
    	try
    	{
     		prepStatement = con.prepareStatement(actor);
    		rS =prepStatement.executeQuery(actor);
    	            
    		while(rS.next())
    		{
    			actorModel.addElement(rS.getString("ACTORNAME"));
    		}
    		prepStatement.close();
    		rS.close();         
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	actorJList.setModel(actorModel);
    	             
    	MouseListener mouseListener = new MouseAdapter() 
    	{
    		@SuppressWarnings("unchecked")
    		public void mouseClicked(MouseEvent e) 
    		{
    			if (e.getClickCount() == 1)
    			{
    				actorList = (ArrayList<String>) actorJList.getSelectedValuesList();
    			}	
    		}
    	};
    	actorJList.addMouseListener(mouseListener);

    	ResultSet rs2 = null;

        movieModel.clear();
        
        try
        {
            prepStatement2 = con.prepareStatement(resultQuery);
            rs2 =prepStatement2.executeQuery(resultQuery);
               
            movieModel.clear();
            movieModel.addElement("ID");
            while(rs2.next())
            {
                movieModel.addElement(rs2.getString("MOVIEID"));
            }
            prepStatement2.close();
            rs2.close();  
        }
        catch(Exception ex2)
        {
            ex2.printStackTrace();
        }
    movieQJList.setModel(movieModel);
    }
    
    private void populateDirector(ActionEvent evt)
    {  	
    	String director = "";
    	String bAttr = "";
    	movieC = "";
    		
    	if(and_or_comboBox.getSelectedIndex() == 0 || and_or_comboBox.getSelectedIndex() == 1)
    	{
    		bAttr = "INTERSECT";
    	}
    	else
    	{
    		if(and_or_comboBox.getSelectedIndex() == 2)
    		{
    			bAttr = "UNION";
    		}
    	}
    		  
    	if(countryList.size() == 0)
    	{
    		int start = 0;
    		int end = countryJList.getModel().getSize()-1;
    		countryJList.setSelectionInterval(start, end);
    		countryList = (ArrayList<String>) countryJList.getSelectedValuesList();	
    	}
    		  
    	int i=0;
    			
    	for(i=0;i<countryList.size()-1;i++)
    	{  		
    		director += "SELECT DISTINCT MD.DIRECTORNAME \nFROM MOVIE_DIRECTORS MD, MOVIE_COUNTRIES MMC\nWHERE MD.MOVIEID= MMC.MOVIEID AND MMC.COUNTRY='"+countryList.get(i)+"' AND MD.MOVIEID IN("+movie+")"+"\n"+bAttr+"\n";
    		movieC += "SELECT DISTINCT MD.MOVIEID\nFROM MOVIE_DIRECTORS MD, MOVIE_COUNTRIES MMC\nWHERE MD.MOVIEID= MMC.MOVIEID AND MMC.COUNTRY='"+countryList.get(i)+"' AND MD.MOVIEID IN("+movie+")"+"\n"+bAttr+"\n";
    	}    

    	director += "SELECT DISTINCT MD.DIRECTORNAME\nFROM MOVIE_DIRECTORS MD, MOVIE_COUNTRIES MMC\nWHERE MD.MOVIEID= MMC.MOVIEID AND MMC.COUNTRY='"+countryList.get(i)+"' AND MD.MOVIEID IN("+movie+")"+"\n";
    	movieC += "SELECT DISTINCT MD.MOVIEID\nFROM MOVIE_DIRECTORS MD, MOVIE_COUNTRIES MMC\nWHERE MD.MOVIEID= MMC.MOVIEID AND MMC.COUNTRY='"+countryList.get(i)+"' AND MD.MOVIEID IN("+movie+")"+"\n";
    			
    	System.out.println(movieC);
    	
    	resultQuery = "";
    	resultQuery2 = "";
    	for(i=0;i<countryList.size()-1;i++)
    	{ 			
    		resultQuery = "SELECT DISTINCT MR.MOVIEID FROM MOVIES MR, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR \nWHERE MR.MOVIEID = MCR.MOVIEID AND MR.MOVIEID = MGR.MOVIEID AND MR.MOVIEID IN ( " + movieC + " ) \n";
    	}    
    	   
		resultQuery = "SELECT DISTINCT MR.MOVIEID FROM MOVIES MR, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR \nWHERE MR.MOVIEID = MCR.MOVIEID AND MR.MOVIEID = MGR.MOVIEID AND MR.MOVIEID IN ( " + movieC + " ) \n";
		
    	ResultSet rS = null;
    	directorModel.clear();
    	          
    	try
    	{   	               
    		prepStatement = con.prepareStatement(director);
    		rS =prepStatement.executeQuery(director);
    		
    		while(rS.next())
    		{
    			directorModel.addElement(rS.getString("DIRECTORNAME"));
    		}
    		prepStatement.close();
    		rS.close();  	                
    	}
    	catch(Exception ex)
    	{
    		ex.printStackTrace();
    	}
    	directorJList.setModel(directorModel);
    	             
    	MouseListener mouseListener = new MouseAdapter() 
    	{
    		@SuppressWarnings("unchecked")
    		public void mouseClicked(MouseEvent e) 
    		{
    			if (e.getClickCount() == 1)
    			{
    				directorList = (ArrayList<String>) directorJList.getSelectedValuesList();
    			}	
    		}
    	};
    	directorJList.addMouseListener(mouseListener);

    	ResultSet rs2 = null;

        movieModel.clear();
        
        try
        {
            prepStatement2 = con.prepareStatement(resultQuery);
            rs2 =prepStatement2.executeQuery(resultQuery);
               
            movieModel.clear();
            movieModel.addElement("ID");
            while(rs2.next())
            {  
                movieModel.addElement(rs2.getString("MOVIEID"));
            }
            prepStatement2.close();
            rs2.close();    
        }
        catch(Exception ex2)
        {
            ex2.printStackTrace();
        }
    movieQJList.setModel(movieModel);
    
    }
    
    private void populateTags(ActionEvent evt)
    {
    	tags = "";
    	movie = "";
		// TODO Auto-generated method stub
		  
		if(genreList.size()!=0)
		{
			String bAttr = "";
	        
			if(and_or_comboBox.getSelectedIndex() == 0 || and_or_comboBox.getSelectedIndex() == 1)
			{
				bAttr = "INTERSECT";
			}
			else
			{
				if(and_or_comboBox.getSelectedIndex() == 2)
				{
					bAttr = "UNION";
				}
			}
			
			int i=0;
			if(start.length() == 4 && stop.length() == 4)
			{
				for(i=0;i<genreList.size()-1;i++)
	  			{
	  				movie += "SELECT DISTINCT M.MOVIEID\nFROM MOVIE_GENRES MG, MOVIES M, MOVIE_TAGS MT\nWHERE M.MOVIEID=MG.MOVIEID AND M.MOVIEID=MT.MOVIEID AND M.MYEAR >='"+ start +"' AND M.MYEAR <='"+ stop +"' AND MG.GENRE = '"+genreList.get(i)+"'\n"+bAttr+"\n";
	  			}
				
	  			movie += "SELECT DISTINCT M.MOVIEID\nFROM MOVIE_GENRES MG, MOVIES M, MOVIE_TAGS MT\nWHERE M.MOVIEID=MG.MOVIEID AND M.MOVIEID=MT.MOVIEID AND M.MYEAR >='"+ start +"' AND M.MYEAR <='"+ stop +"' AND MG.GENRE = '"+genreList.get(i)+"'";
			}
			else
			{
				for(i=0;i<genreList.size()-1;i++)
	  			{

	  				movie += "SELECT DISTINCT M.MOVIEID\nFROM MOVIE_GENRES MG, MOVIES M, MOVIE_TAGS MC\nWHERE M.MOVIEID=MG.MOVIEID AND M.MOVIEID=MT.MOVIEID AND MG.GENRE = '"+genreList.get(i)+"'\n"+bAttr+"\n";
	  			}

	  			movie += "SELECT DISTINCT M.MOVIEID\nFROM MOVIE_GENRES MG, MOVIES M, MOVIE_TAGS MC\nWHERE M.MOVIEID=MG.MOVIEID AND M.MOVIEID=MT.MOVIEID AND MG.GENRE = '"+genreList.get(i)+"'";
			}
			
			tags += "SELECT DISTINCT MT.TAGID,T.VALUE FROM MOVIE_TAGS MT, TAGS T \n WHERE MT.TAGID=T.TAGID AND MT.MOVIEID IN ( " + movie +" ) \n";

			System.out.println(tags);
						
			resultQuery = "SELECT DISTINCT MR.MOVIEID FROM MOVIES MR, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR \nWHERE MR.MOVIEID = MCR.MOVIEID AND MR.MOVIEID = MGR.MOVIEID AND MR.MOVIEID IN ( " + movie + " ) \n";

			ResultSet rS = null;
			tagModel.clear();
			
			try
			{
				prepStatement = con.prepareStatement(tags);
				rS =prepStatement.executeQuery(tags);
	               
				tagModel.clear();  
	  	        tagModel.addElement("TAGID - VALUE");
	  			while(rS.next())
	  			{
	  				tagModel.addElement(rS.getString("TAGID") + " - "+ rS.getString("VALUE"));
	  			}
				prepStatement.close();
				rS.close();
	                
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			tagsValueJList.setModel(tagModel);
	               
			MouseListener mouseListener = new MouseAdapter() 
			{
				public void mouseClicked(MouseEvent e) 
				{
					if (e.getClickCount() == 1)
					{
						tagList = (ArrayList<String>) tagsValueJList.getSelectedValuesList();
						System.out.print(tagList);
					}	
				}
			};
			tagsValueJList.addMouseListener(mouseListener);
			
			ResultSet rs2 = null;
	        
            movieModel.clear();
            
            try
            {
                prepStatement2 = con.prepareStatement(resultQuery);
                rs2 =prepStatement2.executeQuery(resultQuery);
                   
                movieModel.clear();
                movieModel.addElement("ID");
                while(rs2.next())
                {
                        
                    movieModel.addElement(rs2.getString("MOVIEID"));

                }
                prepStatement2.close();
                rs2.close();
                    
            }
            catch(Exception ex2)
            {
                ex2.printStackTrace();
            }
        movieQJList.setModel(movieModel);
		}
    }
    private void populateMovieSearch(ActionEvent evt)
    {       
    	System.out.println(resultQuery);
    	
        ResultSet rs = null;
    
        movieModel.clear();
        
        try
        {
            prepStatement = con.prepareStatement(resultQuery);
            rs =prepStatement.executeQuery(resultQuery);
               
            movieModel.clear();
            movieModel.addElement("ID");
            while(rs.next())
            {
                    
                movieModel.addElement(rs.getString("MOVIEID"));

            }
            prepStatement.close();
            rs.close();
                
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    movieQJList.setModel(movieModel);
    
    }
    	
    private void populateMovieDetails(ActionEvent evt)
    {       
    	MouseListener mouseListener = new MouseAdapter() 
        {
        	public void mouseClicked(MouseEvent e) 
        	{
        		if (e.getClickCount() == 1)
        		{
        			movieList = (ArrayList<String>) movieQJList.getSelectedValuesList();
        		}
        	}
        };
        
        movieQJList.addMouseListener(mouseListener); 
    
        String bAttr = "";
      
		if(and_or_comboBox.getSelectedIndex() == 0 || and_or_comboBox.getSelectedIndex() == 1)
		{
			bAttr = "INTERSECT";
		}
		else
		{
			if(and_or_comboBox.getSelectedIndex() == 2)
			{
				bAttr = "UNION";
			}
		}
		String detailQuery="";
		int i=0;
		for( i=0;i < movieList.size() - 1;i++)
		{
			detailQuery = "SELECT DISTINCT M.MOVIEID, M.TITLE, M.MYEAR, M.RTAUDIENCERATING, M.RTAUDIENCENUMRATING \n FROM MOVIES M \n WHERE M.MOVIEID = '"+movieList.get(i)+"' \n"+bAttr+"\n";
		}
		detailQuery = "SELECT DISTINCT M.MOVIEID, M.TITLE, M.MYEAR, M.RTAUDIENCERATING, M.RTAUDIENCENUMRATING \n FROM MOVIES M \n WHERE M.MOVIEID = '"+movieList.get(i)+"' \n";

		System.out.println(detailQuery);
	
		ResultSet rs2 = null;

		movieDetailModel.clear();
    
		try
		{
			prepStatement2 = con.prepareStatement(detailQuery);
			rs2 =prepStatement2.executeQuery(detailQuery);
           
			movieDetailModel.clear();
			movieDetailModel.addElement("ID - TITLE - YEAR - RATING - NUMRATINGS");
			while(rs2.next())
			{
				movieDetailModel.addElement(rs2.getString("MOVIEID") + " - " +rs2.getString("TITLE")+" - " +rs2.getString("MYEAR")+" - " +rs2.getString("RTAUDIENCERATING")+" - " +rs2.getString("RTAUDIENCENUMRATING"));

			}
			prepStatement2.close();
			rs2.close();
            
		}
		catch(Exception ex2)
		{
			ex2.printStackTrace();
		}
		movieDQJList.setModel(movieDetailModel);
    }
    	
   public void filterTags(ActionEvent evt) 
   {
	   String bAttr = "";
  	        
	   if(and_or_comboBox.getSelectedIndex()== 0 || and_or_comboBox.getSelectedIndex()== 1)
	   {
		   bAttr = "INTERSECT";
	   }
	   else
	   {
		   if(and_or_comboBox.getSelectedIndex()==2)
		   {
			   bAttr = "UNION";
		   }
	   }
	   String val = "";
    	
	   if(tag_wgt_comboBox.getSelectedIndex() == 0)
	   {
		   val = "=";
	   }
	   else if(tag_wgt_comboBox.getSelectedIndex() == 1)
	   {
		   val = ">";
	   }
	   else
	   {
		   val = "<";
	   }
	   String oldResult = "";
	   int k;
	   for(k=0;k<genreList.size()-1;k++)
	   {
		   oldResult += "SELECT DISTINCT MR.MOVIEID\nFROM MOVIES MR, MOVIE_GENRES MG\nWHERE MG.MOVIEID = MR.MOVIEID AND MG.GENRE = '"+ genreList.get(k)+"' AND MR.MOVIEID IN("+movieC+") \n"+bAttr+"\n";
	   }
	   oldResult += "SELECT DISTINCT MR.MOVIEID\nFROM MOVIES MR, MOVIE_GENRES MG\nWHERE MG.MOVIEID = MR.MOVIEID AND ML.GENRE = '"+ genreList.get(k)+"' AND MR.MOVIEID IN("+movieC+") \n";
		
	   resultQuery = "";
	   int i;
	   if(value.length() > 0)
	   {
		   for(i = 0; i < tagList.size() - 1; i++)
		   {
			   resultQuery += "SELECT DISTINCT MR2.MOVIEID\nFROM MOVIES MR2, MOVIE_COUNTRIES MCR, TAGS T, MOVIE_TAGS MT\nWHERE MR2.MOVIEID = MT.MOVIEID AND T.TAGID = MT.TAGID AND MR2.MOVIEID = MCR.MOVIEID AND T.VALUE = '" + tagList.get(i) + "' AND MT.TAGWEIGHT " + val + " '" + value + "' AND MR2.MOVIEID IN("+oldResult+") \n"+bAttr+"\n";
		   }
		   resultQuery += "SELECT DISTINCT MR2.MOVIEID\nFROM MOVIES MR2, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR, TAGS T, MOVIE_TAGS MT\nWHERE MR2.MOVIEID = MT.MOVIEID AND T.TAGID = MT.TAGID AND MR2.MOVIEID = MCR.MOVIEID AND T.VALUE = '" + tagList.get(i) + "' AND MT.TAGWEIGHT " + val + " '" + value + "' AND MR2.MOVIEID IN("+oldResult+")";
	   }
	   else
	   {
		   for(i = 0; i < tagList.size() - 1; i++)
		   {
				resultQuery += "SELECT DISTINCT MR2.MOVIEID\nFROM MOVIES MR2, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR, TAGS T, MOVIE_TAGS MT\nWHERE MR2.MOVIEID = MT.MOVIEID AND T.TAGID = MT.TAGID AND MR2.MOVIEID = MCR.MOVIEID AND T.VALUE = '" + tagList.get(i) + "' AND MR2.MOVIEID IN("+oldResult+") \n"+bAttr+"\n";
		   }
		   resultQuery += "SELECT DISTINCT MR2.MOVIEID\nFROM MOVIES MR2, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR, TAGS T, MOVIE_TAGS MT\nWHERE MR2.MOVIEID = MT.MOVIEID AND T.TAGID = MT.TAGID AND MR2.MOVIEID = MCR.MOVIEID AND T.VALUE = '" + tagList.get(i) + "' AND MR2.MOVIEID IN("+oldResult+")";
	   }
		
	   tags += "SELECT DISTINCT MT.TAGID,T.VALUE FROM MOVIE_TAGS MT, TAGS T \n WHERE MT.TAGID=T.TAGID AND MT.MOVIEID IN ( " + resultQuery +" ) \n";

	   System.out.println(tags);

	   ResultSet rS = null;
	   tagModel.clear();
		
	   try
	   {
		   prepStatement = con.prepareStatement(tags);
		   rS =prepStatement.executeQuery(tags);
               
		   tagModel.clear();  
  	       tagModel.addElement("TAGID - VALUE");
  	       while(rS.next())
  	       {
  	    	   tagModel.addElement(rS.getString("TAGID") + " - "+ rS.getString("VALUE"));
  	       }
  	       prepStatement.close();
  	       rS.close();
                
	   }
	   catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   tagsValueJList.setModel(tagModel);
               
	   MouseListener mouseListener = new MouseAdapter() 
	   {
		   public void mouseClicked(MouseEvent e) 
		   {
			   if (e.getClickCount() == 1)
			   {
				   tagList = (ArrayList<String>) tagsValueJList.getSelectedValuesList();
				   System.out.print(tagList);
			   }	
		   }
	   };
	   tagsValueJList.addMouseListener(mouseListener);
		
	   ResultSet rs2 = null;
        
       movieModel.clear();
        
       try
       {
           prepStatement2 = con.prepareStatement(resultQuery);
           rs2 =prepStatement2.executeQuery(resultQuery);
               
           movieModel.clear();
           movieModel.addElement("ID");
           while(rs2.next())
           {             
               movieModel.addElement(rs2.getString("MOVIEID"));

           }
           prepStatement2.close();
           rs2.close();
                
       }
       catch(Exception ex2)
       {
           ex2.printStackTrace();
       }
       movieQJList.setModel(movieModel);
   }       
     
   private void populateUserQuery(ActionEvent evt)
   {         
	   MouseListener mouseListener = new MouseAdapter() 
       {
		   public void mouseClicked(MouseEvent e) 
		   {
			   if (e.getClickCount() == 1)
			   {
				   movieList = (ArrayList<String>) movieQJList.getSelectedValuesList();
			   }
		   }
       };
         
       movieQJList.addMouseListener(mouseListener);

       String user = "";
       movieC = "";
   		// TODO Auto-generated method stub
       String bAttr = "";
       if(movieList.size()!=0)
       {
    	   if(and_or_comboBox.getSelectedIndex() == 0 || and_or_comboBox.getSelectedIndex() == 1)
    	   {
    		   bAttr = "INTERSECT";
    	   }
    	   else
    	   {
    		   if(and_or_comboBox.getSelectedIndex() == 2)
    		   {
    			   bAttr = "UNION";
    		   }
    	   }		
       }
    		  
       if(movieList.size() == 0)
       {
    	   int start = 0;
    	   int end = movieQJList.getModel().getSize()-1;
    	   movieQJList.setSelectionInterval(start, end);
    	   movieList = (ArrayList<String>) movieQJList.getSelectedValuesList();	
       }
    		  
       int i = 0;
    			
       for( i = 0; i < movieList.size() - 1; i++ )
       {    		
    	   user += "SELECT DISTINCT UTM.USERID \nFROM USER_TAGGEDMOVIES UTM, MOVIE_TAGS MT \nWHERE UTM.MOVIEID= MT.MOVIEID AND MT.MOVIEID='"+movieList.get(i)+"' AND UTM.MOVIEID IN("+movie+")"+"\n"+bAttr+"\n";
    	   movieC += "SELECT DISTINCT UTM.MOVIEID \nFROM USER_TAGGEDMOVIES UTM, MOVIE_TAGS MT \nWHERE UTM.MOVIEID= MT.MOVIEID AND MT.MOVIEID='"+movieList.get(i)+"' AND UTM.MOVIEID IN("+movie+")"+"\n"+bAttr+"\n";
       }    

       user += "SELECT DISTINCT UTM.USERID \nFROM USER_TAGGEDMOVIES UTM, MOVIE_TAGS MT \nWHERE UTM.MOVIEID= MT.MOVIEID AND MT.MOVIEID='"+movieList.get(i)+"' AND UTM.MOVIEID IN("+movie+")"+"\n"+"\n";
       movieC += "SELECT DISTINCT UTM.MOVIEID \nFROM USER_TAGGEDMOVIES UTM, MOVIE_TAGS MT \nWHERE UTM.MOVIEID= MT.MOVIEID AND MT.MOVIEID='"+movieList.get(i)+"' AND UTM.MOVIEID IN("+movie+")"+"\n"+"\n";
    			
       System.out.println(movieC);
    	
       resultQuery = "";
    	
       for( i = 0; i < movieList.size() - 1; i++ )
       {
    	   resultQuery = "SELECT DISTINCT MR.MOVIEID FROM MOVIES MR, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR \nWHERE MR.MOVIEID = MCR.MOVIEID AND MR.MOVIEID = MGR.MOVIEID AND MR.MOVIEID IN ( " + movieC + " ) \n";
       }    
    			
       resultQuery = "SELECT DISTINCT MR.MOVIEID FROM MOVIES MR, MOVIE_GENRES MGR, MOVIE_COUNTRIES MCR \nWHERE MR.MOVIEID = MCR.MOVIEID AND MR.MOVIEID = MGR.MOVIEID AND MR.MOVIEID IN ( " + movieC + " ) \n";
    	
       ResultSet rS = null;
       userModel.clear();
    	          
       try
       {   		
    	   prepStatement = con.prepareStatement(user);
    	   rS =prepStatement.executeQuery(user);
    	            
    	   while(rS.next())
    	   {
    		   userModel.addElement(rS.getString("USERID"));

    	   }
    	   prepStatement.close();
    	   rS.close();
    	                
       }
       catch(Exception ex)
       {
    	   ex.printStackTrace();
       }
       userQJList.setModel(userModel);
   }
}
