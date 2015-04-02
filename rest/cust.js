$(document).ready(function() {
    var output = "<table>";

    $.ajax({
        url: "http://198.23.75.155:27018/stores_demo/customer"
    }).then(function(data) {
       for(var i in data) {
          output += "<tr>";

          $('.greeting-id').append(data[i].customer_num +" ");
          $('.greeting-content').append(data[i].fname + " ");

	  output += "<td>" + data[i].customer_num + "</td>";		
	  output += "<td>" + data[i].fname + "</td></tr>";		


       }
       output += "</table>";
       $('.extra-body').append(output);

    });
});
