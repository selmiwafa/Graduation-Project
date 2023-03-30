import smtplib

# Set up the email addresses and message content
from_email = 'healthbuddyapplication@gmail.com'
to_email = 'bouabidiamina4@gmail.com'
subject = 'Verification Email'
body = 'Your verification code is #993887 .'

# Set up the SMTP server and login credentials
smtp_server = 'smtp.gmail.com'
smtp_port = 587
username = 'healthbuddyapplication@gmail.com'
password = 'fvxtqvvyaumlrxde'

# Create the email message
message = f"""\
From: {from_email}
To: {to_email}
Subject: {subject}

{body}
"""

# Send the email
try:
    server = smtplib.SMTP(smtp_server, smtp_port)
    server.starttls()
    server.login(username, password)
    server.sendmail(from_email, to_email, message)
    print('Email sent successfully!')
except Exception as e:
    print('An error occurred:', e)
finally:
    server.quit()
