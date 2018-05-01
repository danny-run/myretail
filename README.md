# myretail
A demo project of myretail service

# endpoints
All the endpoints are for products so they start with /products
GET /products/{id} provides details of product id, including id, current price, currency code and name. The product name is retrieved from an external URL, which requires an HTTP to HTTPS redirect. In case that the product is not found or the product name is not provided by the external URL. An corresponding error message is return.



