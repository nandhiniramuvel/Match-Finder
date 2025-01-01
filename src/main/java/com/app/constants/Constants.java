package com.app.constants;

import java.util.Arrays;
import java.util.List;

public class Constants {
	public static final int CHUNK_SIZE = 1000; // Number of lines per chunk
	public static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();; // Number of matcher threads
	public static final List<String> NAMES = Arrays.asList(
        "James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas",
        "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian",
        "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
        "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
        "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"
    );
    public static final String FILE_PATH= "src/main/resources/big.txt";
	
}
