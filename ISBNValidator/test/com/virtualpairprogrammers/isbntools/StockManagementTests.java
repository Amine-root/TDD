package com.virtualpairprogrammers.isbntools;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StockManagementTests {

	ExternalISBNDataService testWebService;
	ExternalISBNDataService testDatabaseService;
	StockManager stockManager;
	
	@BeforeEach
	public void setup() {
		testWebService = mock(ExternalISBNDataService.class);
		testDatabaseService = mock(ExternalISBNDataService.class);
		stockManager = new StockManager();
		stockManager.setWebService(testWebService);
		stockManager.setDatabaseService(testDatabaseService);
	}
	
	
	@Test
	public void testCanGetACorrectLocatorCode() {
		
//		ExternalISBNDataService testWebService = new ExternalISBNDataService() {
//			
//			@Override
//			public Book lookup(String isbn) {
//				return new Book(isbn, "Of Mice And Men", "J. Steinbeck");
//			}
//		};
//		
//		ExternalISBNDataService testDatabaseService = new ExternalISBNDataService() {
//			
//			@Override
//			public Book lookup(String isbn) {
//				return null;
//			}
//		};
		
		when(testWebService.lookup(anyString())).thenReturn(new Book("0140177396", "Of Mice And Men", "J. Steinbeck"));
		when(testDatabaseService.lookup(anyString())).thenReturn(null);

		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		
		assertEquals("7396J4", locatorCode);
	}
	
	@Test
	public void databaseIsUsedIfDataIsPresent() {
				
		when(testDatabaseService.lookup("0140177396")).thenReturn(new Book("0140177396", "abc", "abc"));
				
		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		
		verify(testDatabaseService, times(1)).lookup("0140177396");
		verify(testWebService, times(0)).lookup(anyString());
		
	}
	
	@Test
	public void webserviceIsUsedIfDataIsNotPresentInDatabase() {
	
		when(testDatabaseService.lookup("0140177396")).thenReturn(null);
		when(testWebService.lookup("0140177396")).thenReturn(new Book("0140177396", "abc", "abc"));
			
		String isbn = "0140177396";
		String locatorCode = stockManager.getLocatorCode(isbn);
		
		verify(testDatabaseService, times(1)).lookup("0140177396");
		verify(testWebService, times(1)).lookup("0140177396");
	}
	
	
	
	

}
