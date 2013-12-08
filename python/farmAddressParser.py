

import urllib.parse
import urllib.request
from pyquery import PyQuery as pq
import sys

#if argc > 1 set the farm name by argv[1]
if len(sys.argv) > 1:
	print(sys.argv[1])
	farmName = sys.argv[1]
else :
	farmName = ''

#set the parameter of the header of the request
url = 'http://www.i-organic.org.tw/SearchResult.aspx'
values = {'hdnOp':'SearchModeII','FarmName':farmName}
headers = {}

#send the request by urllib
data  = urllib.parse.urlencode(values)
data = data.encode('utf-8')
req = urllib.request.Request(url, data, headers)
response = urllib.request.urlopen(req)
the_page = response.read().decode('utf-8')

#get the href of the farm
query_result = pq(the_page)
farmInfoUrl = query_result('.list_content td a').attr('href')

#if found the href, get the addres of the farm
if farmInfoUrl is not None:
	query_address = pq(farmInfoUrl)
	address = query_address("td[colspan='5']").html()
	print(address)

