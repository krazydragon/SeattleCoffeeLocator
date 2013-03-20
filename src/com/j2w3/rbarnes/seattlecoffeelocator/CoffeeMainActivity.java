/*
 * project	SeattleCoffeeLocator
 * 
 * package	com.j2w3.rbarnes.seattlecoffeelocator
 * 
 * @author	Ronaldo Barnes
 * 
 * date		Mar 20, 2013
 */
package com.j2w3.rbarnes.seattlecoffeelocator;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class CoffeeMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this, CoffeeService.class));
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
