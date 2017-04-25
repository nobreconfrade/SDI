require 'rest-client'

url = "http://localhost:9876/hw?wsdl"
service = RestClient.get(url, qname)
# print service
# service.sayHello("sera?")
