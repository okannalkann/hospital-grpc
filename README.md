
# Hospital Service(localhost:8082/api)

| Route | HTTP Verb	 | POST body	 | Description	 |
| --- | --- | --- | --- |
| /hospital/{hospital_id} | `GET` | Empty | Get Hospital by Id. |
| /hospital/ | `GET` | Empty | List all Hospitals. |
| /hospital/ | `POST` | {"name":"Großhadern Clinic", "address":"Marchioninistraße 15, 81377 München", "phone":"089 44000", "capacity":10} | Create a hospital. |
| /hospital/{hospital_id} | `PUT` | {"name":"Okanhadern Clinic", "address":"Marchioninistraße 15, 81377 München", "phone":"089 44000", "capacity":10 } | Update a hospital with new info. |
| /hospital/{hospital_id} | `DELETE` | Empty | Delete a hospital. |

# Patient Service (localhost:8080/api)

| Route | HTTP Verb	 | POST body	 | Description	 |
| --- | --- | --- | --- |
| /patient/{patient_id} | `GET` | Empty | Get patient by Id. |
| /patient | `GET` | Empty | List all patients. |
| /patient/getHospitalPatients/{hospital_id} | `GET` | Empty | Get hospital patients. |
| /patient/hospitalcheck/{hospital_id} | `GET` | Empty | Check hospital exist. |
| /patient | `POST` | {     "firstname":"Okan", "lastname":"Alkan","dateofbirth":"27.07.1995", "gender":"Male","address":"Marchioninistraße 15, 81377 München", "phone":"01573472995151", "email":"okann.alkann@gmail.com", "emergencycontact":"123123123"} | Create a patient. |
| /patient/register | `POST` | { "patientId": "1", "hospitalId": "1","dateRegistered": "2024-05-11","dateDischarged": "2024-05-12" } | Register Patient to hospital. |
| /patient/{patient_id} | `PUT` | { "firstname":"Okan", "lastname":"Alkan 2", "dateofbirth":"20.07.1995", "gender":"Malee","address":"Marchioninistraße 15, 81377 München", "phone":"089 44000","email":"alkano@tum.de", "emergencycontact":"1231231234"} | Update Patient. |
| /patient/{patient_id} | `DELETE` | Empty | Dalete Patient. |
