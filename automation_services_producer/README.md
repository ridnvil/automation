# Service For Publish Data to Kafka

After running you need to config host and port Kafka server with curl or using postman with method POST:

path is "http://your-server-running/kafka/producer"
```
{
    "host": "your host of kafka server",
    "port": your port for kafka server
}
```

And for sample Data to send in kafka like this:

```
{
 	"bank": "your bank name",
 	"username": "your username",
 	"mpin": "your mpin",
 	"rekening": "to bank number",
 	"nominal": hw much the money,
 	"password": "your password",
 	"udid": "your device ID",
 	"port": your port
 }
```