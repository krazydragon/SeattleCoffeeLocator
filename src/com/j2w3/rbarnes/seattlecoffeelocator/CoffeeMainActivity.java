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

import com.j2w3.rbarnes.seattlecoffeelocator.CoffeeListFragment.OnLocationSelectedListener;

import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

public class CoffeeMainActivity extends FragmentActivity implements OnLocationSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startService(new Intent(this, CoffeeService.class));
		setContentView(R.layout.activity_coffee_main);
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onlocationSelected() {
		// TODO Auto-generated method stub
		
	}

	

}
