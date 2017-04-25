require 'rest-client'

# url = "http://localhost:9876/hw?wsdl"
# service = RestClient.get(url)
# print service
# service.sayHello("sera?")
RestClient.get('http://localhost:9876/hw?wsdl') { |response, request, result, &block|
  case response.code
  when 200
    p "API esta funcionando"
    response
  when 423
    raise "Error 423"
  else
    response.return!(request, result, &block)
  end
}
