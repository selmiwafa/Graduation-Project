import yagmail

# create a yagmail instance with SSL encryption
yag = yagmail.SMTP('healthbuddyapplication@gmail.com', 'jehwdflnlmaablwr', smtp_ssl=True)

# send the email
to = 'selmiwafaaa@gmail.com'
subject = 'Test Email'
body = 'Hello, this is a test email sent using yagmail with SSL!'
attachments = ['/path/to/attachment1', '/path/to/attachment2']
yag.send(to=to, subject=subject, contents=body, attachments=attachments)

# close the yagmail instance
yag.close()
