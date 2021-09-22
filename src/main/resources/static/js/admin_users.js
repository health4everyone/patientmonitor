$( document ).ready(function() {
	$('#paginatedTable').DataTable( {
        "processing": true,
        "serverSide": true,
        "pageLength": 10,
        "search": {
        	return: true
        	},
        "ajax": {
            "url": "/users/paginated",
            "data": function ( data ) {
			 //process data before sent to server.
         }},
        "columns": [
                    { "data": "id", "name" : "ID", "title" : "ID"  , "type":"num" ,"searchable":false},
                    { "data": "email", "name" : "Email" , "title" : "Email", "type":"string" },
                    { "data": "enabled", "name" : "enabled" , "title" : "Active" },
                    { "data": "createdAt", "name" : "createdAt" , "title" : "Created", type: "date", orderable:false, "searchable":false},
                    { "data": "lastSeen", "name" : "lastSeen" , "title" : "Last seen", type: "date", "defaultContent": "Never", orderable:false, "searchable":false}
                    
                ]
	});
	
	$('#paginatedTable').dataTable().fnSetFilteringEnterPress();
});