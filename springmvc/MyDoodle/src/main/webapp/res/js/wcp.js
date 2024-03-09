// wcp.js

(function( wcp ) {
	"use strict";
	
	// Namespace trick explained here: http://stackoverflow.com/a/5947280/587641
	// For a public property or function, use "wcp.xxx = ..."
	// For a private property use "var xxx = "
	// For a private function use "function xxx(..."

	wcp.getOrCreateUUID = function() {
	  // Try to retrieve the UUID from a cookie
	  let uuid = getCookie('uuid');
	  
	  // If it's not found, generate a new UUID, set the cookie, and return the UUID
	  if (!uuid) {
	    uuid = generateUUID();
	    setCookie('uuid', uuid, 7); // Set the cookie to expire in 7 days
	  }
	  
	  return uuid;
	}
	// Helper function to generate a UUID
	function generateUUID() {
	  // Generate a random number and convert it to base 36 (0-9 + a-z),
	  // then take the substring starting from position 2 to get rid of the "0."
	  // and limit the length to 8 characters.
	  return Math.random().toString(36).substr(2, 8);
	}
	
	// Function to set a cookie
	function setCookie(name, value, days) {
	  let expires = "";
	  if (days) {
	    let date = new Date();
	    date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
	    expires = "; expires=" + date.toUTCString();
	  }
	  document.cookie = name + "=" + (value || "") + expires + "; path=/";
	}
	
	// Function to get a cookie by name
	function getCookie(name) {
	  let nameEQ = name + "=";
	  let ca = document.cookie.split(';');
	  for(let i=0;i < ca.length;i++) {
	    let c = ca[i];
	    while (c.charAt(0) == ' ') c = c.substring(1,c.length);
	    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	  }
	  return null;
	}	
	
}( window.wcp = window.wcp || {} ));	
