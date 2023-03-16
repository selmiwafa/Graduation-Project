<?php 

	require_once 'DbConnect.php';
	
	$response = array();
	
	if(isset($_GET['apicall'])){
		
		switch($_GET['apicall']){
			
			case 'signup':
				if(isTheseParametersAvailable(array('email','name','birthdate','password','adress'))){
					$email = $_POST['email']; 
					$name = $_POST['name'];
					$birthdate = $_POST['birthdate'];
					$password = $_POST['password']; 
					&adress = $_POST['adress'];
					
					$stmt = $conn->prepare("SELECT id FROM users WHERE email = ?");
					$stmt->bind_param("s", $email);
					$stmt->execute();
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						$response['error'] = true;
						$response['message'] = 'User already registered';
						$stmt->close();
					}else{
						$stmt = $conn->prepare("INSERT INTO users (email, name, birthdate, password,adress) VALUES (?, ?, ?, ?, ?)");
						$stmt->bind_param("sssss", $email, $name, $birthdate, $password, $adress);

						if($stmt->execute()){
							$stmt = $conn->prepare("SELECT email, name, birthdate, adress FROM users WHERE email = ?"); 
							$stmt->bind_param("s",$email);
							$stmt->execute();
							$stmt->bind_result($email, $name, $birthdate, $adress);
							$stmt->fetch();
							
							$user = array(
								'email'=>$email, 
								'name'=>$name,
								'birthdate'=>$birthdate,
								'adress'=>$adress
							);
							
							$stmt->close();
							
							$response['error'] = false; 
							$response['message'] = 'User registered successfully'; 
							$response['user'] = $user; 
						}
					}
					
				}else{
					$response['error'] = true; 
					$response['message'] = 'required parameters are not available'; 
				}
				
			break; 
			
			case 'login':
				
				if(isTheseParametersAvailable(array('email', 'password'))){
					
					$email = $_POST['email'];
					$password = md5($_POST['password']); 
					
					$stmt = $conn->prepare("SELECT email, name, birthdate, adress FROM users WHERE email = ? AND password = ?");
					$stmt->bind_param("ss",$email, $password);
					
					$stmt->execute();
					
					$stmt->store_result();
					
					if($stmt->num_rows > 0){
						
						$stmt->bind_result($email, $name, $birthdate, $adress);
						$stmt->fetch();
						
						$user = array(
							'email'=>$email,
							'name'=>$name,
							'birthdate'=>$birthdate,
							'adress'=>$adress
						);
						
						$response['error'] = false; 
						$response['message'] = 'Login successfull'; 
						$response['user'] = $user; 
					}else{
						$response['error'] = false; 
						$response['message'] = 'Invalid email or password';
					}
				}
			break; 
			
			default: 
				$response['error'] = true; 
				$response['message'] = 'Invalid Operation Called';
		}
		
	}else{
		$response['error'] = true; 
		$response['message'] = 'Invalid API Call';
	}
	
	echo json_encode($response);
	
	function isTheseParametersAvailable($params){
		
		foreach($params as $param){
			if(!isset($_POST[$param])){
				return false; 
			}
		}
		return true; 
	}