

Request:
~~~
curl --location --request POST 'http://localhost:9090/camel' \
--header 'accept: application/json' \
--header 'X-TASK-NAME: BOOKING' \
--header 'Content-Type: application/json' \
--data-raw '{
    "origin": "CHENNAI",
    "destination": "MUMBAI",
    "date": "20/11/2022",
    "shipper": "ABC",
    "consignee": "XYZ"
}'
~~~