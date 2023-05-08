Attached is a CSV formatted data for wages in the city of Seattle. Given this file please write a script that produces json formatted output that identifies the highest average paying job title along with the department for all unique first names. Please ignore Sr and roman numerals (like II, III).

For example if we have : 

| Department | Last Name | First Name |	Job Title |	Hourly Rate |
| :----------- |:-------------:|:---------:|:-----------:|:-----------:|
| QA | Stargel | Eric | QA Engineer | 35 |
| QA | Miller | Eric | QA Engineer | 40 |
 

The result output should be : 
```json
{
   "Eric": {
      "Department": "QA",
      "Job Title": "QA Engineer",
      "Average Hourly Rate": "37.5"
     }
}
