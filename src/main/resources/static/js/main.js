/* 비밀번호 보이기 */
function onoff(){
	if (document.getElementById('pw-input').type == 'password') {
	        document.getElementById('pw-input').type = 'text';
	        document.getElementById('pw-toggle').src = '/images/off.png'; 
	    } else {
	        document.getElementById('pw-input').type = 'password';
	        document.getElementById('pw-toggle').src = '/images/on.png'; 
	   }
}


