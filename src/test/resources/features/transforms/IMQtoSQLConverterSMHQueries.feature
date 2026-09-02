Feature: IMQ to SQL conversion
  @IMQSMHQueriesTest
  Scenario Outline: IMQ converts to SQL without errors
    When IMQ to SQL conversion is executed for <iri>
    Then SQL should be generated successfully

  Examples:
  | iri                                                              | count | label                                                                                                |
  |"http://smartlifehealth.info/smh#441fa4d1-3160-4b9d-88e6-02f914c1fe83"|   |	"1. Cardio-Renal-Metabolic"                                                                          |
  |"http://smartlifehealth.info/smh#d1439295-9383-45ce-9af1-f59349f38a36"||	"Smart Searches V7 -report"|
  |"http://smartlifehealth.info/smh#68a68d85-e52e-4328-b433-f7e234dc96c8"||	"All patients excluding private patients"|
  |"http://smartlifehealth.info/smh#931e901b-8a0f-41b9-8064-beb3c4e2c2e4"||	"Cardio AF00 Atrial Fibrillation Register_v49_Release_1"|
  |"http://smartlifehealth.info/smh#74198a12-677c-4614-b806-fda7bd754928"||	"Cardio CH00 CHD register version 49 release 1"|
  |"http://smartlifehealth.info/smh#bb5b362f-fdd4-46f5-9f8d-ead00b14ac18"||	"Cardio HF00 -Heart Failure Register version 49 release 1"|
  |"http://smartlifehealth.info/smh#44103a56-4adf-493d-b73f-bf9df0ad36ba"||	"Cardio HT00 - Hypertension Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#e736c24b-77bf-4f01-8a4f-b7b996cac424"||	"Cardio PD00 - Peripheral Vasular Disease Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#7c420252-4e06-4b12-81d6-a7ec6d125c10"||	"Cardio ST00 - Stroke or TIA Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#eba32279-45ec-4abb-90b6-10af84b5875e"||	"Met DM00 - Diabetes Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#d74bcda7-d2f0-4060-97ef-2086eb1cc5b9"||	"Met OB00 - Obesity Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#2dc54f28-3055-4db3-b31a-e79e5981fbd0"||	"Renal CK00 - CKD Register version 49 release1"|
  |"http://smartlifehealth.info/smh#c56bc95d-3237-42f8-903c-1d3517681e05"||	"No diabetes diagnosis or Patient's latest diabetes diagnosis followed by resolved code"|
  |"http://smartlifehealth.info/smh#66622f36-b09f-4a88-bf59-91fb01a8183d"||	"unresolved diabetes diagnosis"|
  |"http://smartlifehealth.info/smh#9b5a8b53-263b-4f50-9912-229065303760"||	"NDH_REG Register - Aged 18 or over with non-diabetic hyperglycaemia"|
  |"http://smartlifehealth.info/smh#59812b33-b915-4293-8bb0-66167eb90190"||	"Met ND00- Non Diabetic HyperGlycaemia Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#461c92eb-df4b-4b35-9a6f-71a4676d9f8e"||	"AF00 Atrial Fibrillation Register_v49_Release_1"|
  |"http://smartlifehealth.info/smh#c7cb88c8-0d64-4025-85ba-c9141eb1eee5"||	"AF01 Patients who have AF Resolved as final code"|
  |"http://smartlifehealth.info/smh#62887b09-843d-44bd-ae7b-196c1c19668d"||	"AF02 Patient on AF code other than those recognised by QOF"|
  |"http://smartlifehealth.info/smh#feff0a68-5e27-4641-8659-55e3b7ac3366"||	"AF03 Patients who have irregular pulse recorded or ECG with irregular pulse"|
  |"http://smartlifehealth.info/smh#2e39c237-945e-40d9-a002-7611884e90cf"||	"AF04 Patients on rate control drugs but no diagnosis of AF - remove verapamil"|
  |"http://smartlifehealth.info/smh#495a7020-7ad2-487a-aae9-6c22a8b4535b"||	"AF05 Patient on Anticoagulant and stroke register but not on AF register"|
  |"http://smartlifehealth.info/smh#0d1f6a56-8d3f-46c0-a848-8d1197533595"||	"AF06 Patient on Anticoagulants but not on Stroke register"|
  |"http://smartlifehealth.info/smh#2abbbb0c-ee13-4aff-9bad-f6531a77d06a"||	"AF07 Patients who have had CHADS or CHADVASC done but no diagnosis of AF"|
  |"http://smartlifehealth.info/smh#10437a69-3d86-4141-95ba-0d4853ec7cc2"||	"STIA001 - Patients on the stroke or TIA register_V46_release1"|
  |"http://smartlifehealth.info/smh#1acb49cf-a299-4232-9210-3848a54deda4"|                                                           |	"AF00 -report"                                                                  |
  |"http://smartlifehealth.info/smh#32df825d-2f32-4612-8239-034ca8cfb358"||	"AF01 -report"|
  |"http://smartlifehealth.info/smh#075679b3-005f-4d58-b3fb-b2a55c5c1693"||	"AF02 -report"|
  |"http://smartlifehealth.info/smh#f2c08c52-47d9-48d4-ba78-7f8f0b130ce0"||	"AF03 -report"|
  |"http://smartlifehealth.info/smh#f8e79baf-4c8c-46eb-8a3f-e2f565c42636"||	"AF04 -report"|
  |"http://smartlifehealth.info/smh#2abde18b-0030-44ff-a7a9-59dd0a6bf608"||	"AF05 -report"|
  |"http://smartlifehealth.info/smh#ecfc4836-0c93-4512-b0e0-c70dd9a8b674"||	"AF06 -report"|
  |"http://smartlifehealth.info/smh#e74f604f-14e1-4e1c-a09f-6405ff03efd7"||	"AF07 -report"                                                                 |
  |"http://smartlifehealth.info/smh#6c673776-8ebc-4aa3-a475-35cafb95a6c7"|                                |	"AS01 Patients with asthma resolved as the final code"|
  |"http://smartlifehealth.info/smh#487c8f6d-bcd6-4f42-adeb-ec10d01ea17a"||	"AS02 Diagnosis of asthma not recognised by QOF"|
  |"http://smartlifehealth.info/smh#1138e233-0fb7-4e83-87e3-7252f4a07b17"||	"AS03 Asthma related code but no diagnosis of asthma"|
  |"http://smartlifehealth.info/smh#2ec45a5e-7f38-4322-ad53-7b3811ea834c"||	"AS04 No diagnosis but have allergic upper resp codes"|
  |"http://smartlifehealth.info/smh#6d669bc7-d466-48a9-8518-b819d34d5630"||	"AS05 COPD resolved code as the latest code"|
  |"http://smartlifehealth.info/smh#fab8879c-c603-4465-8cc4-09f954686794"|            |	"AS06 Patients with possible diagnosis of COPD and not on QOF register"|
  |"http://smartlifehealth.info/smh#647f53c6-4c90-419a-9712-d4a5b231e099"|   |	"AS07 on inhalers with no diagnosis of asthma or COPD with cough or wheeze"|
  |"http://smartlifehealth.info/smh#39b86eed-ea95-4c2d-a5af-6753ee0da33e"||	"AS08 Patients on inhalers but missed in other searches"          |
  |"http://smartlifehealth.info/smh#4b542d70-1634-4d92-8653-326bcfde2756"|                                                                    |	"AS00 Asthma Register v49 Release 1"                                                                      |
  |"http://smartlifehealth.info/smh#5d2ccaa9-6d8d-4bff-b385-cd1c27c623b8"|                                                         |	"AS000 COPD register v49 release 1"                                    |
  |"http://smartlifehealth.info/smh#c556aa97-f441-42df-86c2-4198c9bd1a3c"||	"feno"                                            |
  |"http://smartlifehealth.info/smh#b5ce71df-d506-4c3f-85cb-5f63a67232cd"|                       |	"AS00 -report"                       |
  |"http://smartlifehealth.info/smh#e28a7638-040d-429d-8fd8-cf0da1d1e5af"||	"AS000 -report"|
  |"http://smartlifehealth.info/smh#8f32bd9d-8ba6-4bac-a2df-67dd90ca69e0"||	"AST005 - Patients on the asthma register v47 r1.1"                                                   |
  |"http://smartlifehealth.info/smh#a300ee0f-bd4f-4b6d-9ea4-8ba1ecb1a559"|                                               |	"AS01 -report"                                                       |
  |"http://smartlifehealth.info/smh#86b219ef-8831-45fa-b41a-7f7887475ad1"||	"AS05 -report"                                                    |
  |"http://smartlifehealth.info/smh#ff39a39e-0768-4061-ad1e-20c64ebdfb8c"||	"AS02 -report"|
  |"http://smartlifehealth.info/smh#d46bfb6c-f924-4c68-b5cf-ceab044abce7"||	"AS03 -report"               |
  |"http://smartlifehealth.info/smh#ec5ab3e0-22a4-440e-87f0-363d9f5e5a82"||	"AS04 -report"                                                              |
  |"http://smartlifehealth.info/smh#50fe7feb-8d52-41b7-a03a-3d77b1e99c8b"|            |	"AS06 -report"|
  |"http://smartlifehealth.info/smh#5ad7cc45-46b8-483a-b0eb-10f3c3185928"|                                                     |	"AS07 -report"|
  |"http://smartlifehealth.info/smh#9f3eb19a-d98c-4943-a6c2-c7a324cf27c1"||	"AS08 -report"  |
  |"http://smartlifehealth.info/smh#3574a379-45c2-4e66-9b6c-4e0cf3daa0d1"||	"BP00 BP register v49 release1"|
  |"http://smartlifehealth.info/smh#ddc89ed3-4f96-4814-961f-d545f20b56ec"|         |	"BP01 checking for BP codes other than those recognised by QoF (RD)"                                                   |
  |"http://smartlifehealth.info/smh#cf0fb7dd-d715-4466-a6b7-35d57aad6cf9"||	"BP00 -report"                      |
  |"http://smartlifehealth.info/smh#c5fa931a-dd7b-4ce2-bbda-ce96b889e9c4"|                   |	"BP01 -report"|
  |"http://smartlifehealth.info/smh#7d112dd2-253b-4f38-b344-813b12c63251"|                      |	"CA01 Cancer diagnosis but not on register"|
  |"http://smartlifehealth.info/smh#1a65aef5-12d0-48a8-8cf9-9d2fa56032ff"|                                                          |	"CA02 Oncology cancer administration and suspected cancer codes"|
  |"http://smartlifehealth.info/smh#04ef3b2e-8210-46fa-8303-d487c1fab173"||	"CA9001 - Patients with oncology codes"            |
  |"http://smartlifehealth.info/smh#b8934692-f94e-4a17-b560-79931c872dd5"||	"CA00 - cancer register v49 release1"                                   |
  |"http://smartlifehealth.info/smh#53842294-582c-4797-9521-933a09f8cb67"|   |	"CA00 -report"                                                                    |
  |"http://smartlifehealth.info/smh#f0db5915-dc2b-45cc-a9c1-8659b651dcf1"|                           |	"CA01 -report"|
  |"http://smartlifehealth.info/smh#48390737-f7fb-4a70-b8e6-e6ac26bbbf7c"|                                                         |	"CA02 -report"|
  |"http://smartlifehealth.info/smh#f524fdf5-4617-4a19-abf7-386a0e490393"|                                   |	"CH01 Patients on Ticagralor Cangralo or Prasugral but not on CHD register"                                                          |
  |"http://smartlifehealth.info/smh#fafa55d5-d577-4670-82dc-7678447999b4"|                                                    |	"CH02 Codes highly Suggestive of Ischaemic Heart Disease"           |
  |"http://smartlifehealth.info/smh#254a9d93-7ed3-4f64-8f2d-d50ad8ef819e"|                                            |	"CH03 Patients on Oral Anti Anginal Drugs"                  |
  |"http://smartlifehealth.info/smh#0d5947e2-b6dc-4907-971d-2739e932eda1"|                                                           |	"CH04 Patients on Treatment with tripple therapy and nitrolingual spray"                                                       |
  |"http://smartlifehealth.info/smh#9c6e345b-7eb9-4d1e-b0ae-e771ec1ea6e4"|                                                        |	"CH05 Patients with other codes suggestive of IHD"                                               |
  |"http://smartlifehealth.info/smh#a4f10aaa-1bd0-4e1c-93af-4d045a17f412"|      |	"CH00 CHD register version 49 release 1"|
  |"http://smartlifehealth.info/smh#05778bd3-f043-4586-a2d3-e4e373eb5222"|                |	"CH00 -report"                 |
  |"http://smartlifehealth.info/smh#a4094bfe-fcec-406d-8306-bd8eadee7e17"||	"CH01 -report"                                           |
  |"http://smartlifehealth.info/smh#b19bccd4-0281-44be-badd-b7dcfc6b6fa5"||	"CH02 -report"|
  |"http://smartlifehealth.info/smh#c9174e62-635c-47d5-8d30-e97d1578b2ea"||	"CH03 -report"|
  |"http://smartlifehealth.info/smh#faad1738-4a72-4415-8be3-0d2c95a11f14"|                                            |	"CH04 -report" |
  |"http://smartlifehealth.info/smh#049b0823-06a1-4073-88ab-5fa1d5ed3749"|                                      |	"CH05 -report"        |
  |"http://smartlifehealth.info/smh#b810e920-c2f9-44f9-bbe6-05e3324133ed"|                                                        |	"CK00 - CKD Register version 49 release1"|
  |"http://smartlifehealth.info/smh#3487d4b5-02cf-49d0-8022-b2ad6059736a"|                            |	"CK01 Patient with eGFR less than 60 and not on register"|
  |"http://smartlifehealth.info/smh#0ee60360-1bd9-471c-bc46-4c94cb3bfb21"|          |	"CK02 Patient with eGFR less than 60 and not on register"|
  |"http://smartlifehealth.info/smh#77bd7253-3125-47ba-9dbe-f791d4a97444"|                                            |	"CK00 -report"|
  |"http://smartlifehealth.info/smh#de520c0e-c3f6-4d6f-9005-36406f61e922"||	"CK01 -report"      |
  |"http://smartlifehealth.info/smh#5ff8d7a2-711b-45e7-9691-27d3315a2b3c"||	"CK02 -report"                                 |
  |"http://smartlifehealth.info/smh#aa2e7e87-4a51-4050-bf0a-46e228a629cf"||	"DE00 - Patients on the dementia register version 49 release1"                                                |
  |"http://smartlifehealth.info/smh#8c0b2d22-6c5f-4d9f-871e-8dc1d458a324"|        |	"DE01 On dementia medication but not on the register"                                                      |
  |"http://smartlifehealth.info/smh#2d12afed-29f8-40a4-8db0-813e076a0254"|                        |	"DE02 Not on register but have dementia related codes and not search 1"       |
  |"http://smartlifehealth.info/smh#58110832-e472-48f3-aa67-3076d0b3e2d8"||	"DE03 Memory impairement code but no in register or searches 1,2 or 3"             |
  |"http://smartlifehealth.info/smh#243bde47-a36e-4354-ab00-2e2235cf0d26"||	"DE04 Had memory assessment indicating impairement but no diagnosis"         |
  |"http://smartlifehealth.info/smh#c8423809-5b3d-44fc-815e-ebe660cf8bf7"|         |	"DE00 -report"                                                          |
  |"http://smartlifehealth.info/smh#49501bbb-ec14-49e1-954a-236b2c5f881e"|                                                              |	"DE01 -report"|
  |"http://smartlifehealth.info/smh#e7fca448-7807-4cd6-8356-d4495b9580d4"|                                  |	"DE02 -report"                                                                     |
  |"http://smartlifehealth.info/smh#cf42c194-bab0-4860-a8f4-fea7250459ba"|                                                |	"DE03 -report"                                                    |
  |"http://smartlifehealth.info/smh#8f8328e1-b2eb-4e7c-a97a-87337e92178f"||	"DE04 -report"|
  |"http://smartlifehealth.info/smh#a16d4ae7-680d-43cd-a2af-93f034be1b4a"||	"DP01 currently on treatment for depression with depression resolved code"                                                |
  |"http://smartlifehealth.info/smh#b3167f86-9efa-47fd-b61d-1722d9285bf1"|                         |	"DP02 Depression related code but not on depression or mental health register"                              |
  |"http://smartlifehealth.info/smh#074dd5a8-ac35-4006-a415-e97d1e9d9b03"|                       |	"DP03 Depression related code but not on depression register"                                                      |
  |"http://smartlifehealth.info/smh#a8cbfd7b-7938-4a91-96cf-ae74aeda0200"||	"DP04 On Antidepressants but no depression code and on mental health register"|
  |"http://smartlifehealth.info/smh#9c0cc17b-bacc-45c2-aa0f-0fa5523520e1"||	"DP05 On Antidepressants but no depression code and not on mental health register"|
  |"http://smartlifehealth.info/smh#91e1ef7a-e84b-4d76-8db5-47514d5edd59"||	"DP900 - anxiety search excluding depression and low mood and mood disorders"|
  |"http://smartlifehealth.info/smh#69e663ca-09ac-449a-9625-d12d9beb70a7"|                                                                     |	"DP00 - Depression Register version 49 Release 1"|
  |"http://smartlifehealth.info/smh#374e4230-2e5f-44fb-9b58-b2fbdc272bbe"|                                                    |	"MH00 - Patient on mental health register version 49 release 1"                                     |
  |"http://smartlifehealth.info/smh#b9737bc6-b44a-4d55-8923-1f3e3aa18bb4"||	"DP00 -report"                                                             |
  |"http://smartlifehealth.info/smh#aff8c7cc-fe9a-4228-9604-6f7374917c9e"||	"DP01 -report"                                 |
  |"http://smartlifehealth.info/smh#916d814c-3202-46d3-9753-9ae66564fec0"|                   |	"DP02 -report"           |
  |"http://smartlifehealth.info/smh#4b313b4e-21c4-4645-9cc3-bd996b3081c5"|                             |	"DP03 -report"                           |
  |"http://smartlifehealth.info/smh#12ee70e0-0c05-4278-b4bf-585e856f485d"||	"DP04 -report"|
  |"http://smartlifehealth.info/smh#093e8107-02d0-4af0-a16b-62a854d50f6a"||	"DP05 -report"|
  |"http://smartlifehealth.info/smh#8541c90e-78e0-4065-828f-e50ed45031a6"||	"MH01 On antipshychotics but not on MH register"|
  |"http://smartlifehealth.info/smh#171eac31-74b9-476b-a27e-df6c044eeacf"||	"MH02 Patient with mental health codes but not in register"                                  |
  |"http://smartlifehealth.info/smh#cc1d0cb3-5255-43a8-90cc-372390376fcb"||	"MH9001 - anxiety search excluding depression and low mood and mood disorders"|
  |"http://smartlifehealth.info/smh#3e19d1ab-ed23-4bc7-93e3-a452b992cfad"||	"DP00 - Depression Register version 49 Release 1"                           |
  |"http://smartlifehealth.info/smh#0540341e-180c-4a17-bf49-49a074fbdb4c"||	"MH00 - Patient on mental health register version 49 release 1"                                  |
  |"http://smartlifehealth.info/smh#8e6b1720-5de3-4af2-8fc7-2680d7a6dc04"||	"MH00 -report"                                                                      |
  |"http://smartlifehealth.info/smh#4b763e58-bf5a-4117-928a-4305a9e83f5b"|     |	"MH01 -report"|
  |"http://smartlifehealth.info/smh#6ff79a19-0e14-48a9-a6ba-a0fe0ccd7b9f"|                                                              |	"MH02 -report"                                      |
  |"http://smartlifehealth.info/smh#26bed004-9929-48f5-a242-0bdfc68b5f5f"|                         |	"EP01 coded as epilepsy resolved but on medication"|
  |"http://smartlifehealth.info/smh#e64de273-1710-46b9-a10d-b29bf365fd3d"||	"EP02 Epilepsy related code but not on register"                                                                   |
  |"http://smartlifehealth.info/smh#c8d6e902-f99f-496a-80c7-4d420890f5fd"| |	"EP00 Epilepsy Register version 49 Release1"               |
  |"http://smartlifehealth.info/smh#cf048daf-3dcb-4341-8dec-de685dc00082"|                                                         |	"EP00 -report"                                                |
  |"http://smartlifehealth.info/smh#dea80718-f52c-4b32-99f9-4fa37c0a8627"||	"EP01 -report"|
  |"http://smartlifehealth.info/smh#492d62af-eeba-46de-8228-7959292ad7b0"||	"EP02 -report"|
  |"http://smartlifehealth.info/smh#c86bf95d-9b6f-47e4-8005-1bf247c70328"||	"HF01 Codes strongly suggestive of heart failure"                                                     |
  |"http://smartlifehealth.info/smh#0dc344c0-42be-4ae2-93a6-fc156fb570a2"|                                                 |	"HF02 Heart Failure code not recognised by Qof"|
  |"http://smartlifehealth.info/smh#952b102a-8987-4fbf-a1cb-ad5fe9f091c7"||	"HF03 Cardiomegaly with diuretics but not on HF register"|
  |"http://smartlifehealth.info/smh#616ce354-1be0-4531-ba6b-4d5f47885971"|                                                       |	"HF04 Cardiomyopathy but not on HF register"|
  |"http://smartlifehealth.info/smh#3f24de69-d6ba-45ed-82a4-75d2da3cbad2"|                                     |	"HF05 On CHD or Hypertension register and on tripple therapy"|
  |"http://smartlifehealth.info/smh#71e3175b-ab44-4436-8b9d-aadb7de67d6a"|                  |	"HF06 Not on CHD or hypertension register but have tripple therapy"|
  |"http://smartlifehealth.info/smh#79dd62b3-7653-475e-8215-3e6b4601abcc"|              |	"HF07 On CHD or Hypertension register and on diuretic but not on HF register"|
  |"http://smartlifehealth.info/smh#db78b9c8-2d15-4d66-ab85-45dcbbd91d94"|    |	"CH00 CHD register version 49 release 1"|
  |"http://smartlifehealth.info/smh#94cac8a9-fe4b-4108-8876-560a0cfab589"||	"HT00 - Hypertension register version 49 release 1"|
  |"http://smartlifehealth.info/smh#76c6f2a5-f182-4c3c-931f-ce58c36e2da6"||	"HF00 -Heart Failure Register version 49 release 1"|
  |"http://smartlifehealth.info/smh#9ad1141a-5759-4b70-ac9a-9c0bae617bbf"||	"HF00 -report"                                                                     |
  |"http://smartlifehealth.info/smh#1465700b-aa8f-44e2-825d-3a37e4f3b279"|                       |	"HF01 -report"                                                    |
  |"http://smartlifehealth.info/smh#b557d615-2eef-4613-a354-e4dfd2fae3b3"|                                       |	"HF02 -report"       |
  |"http://smartlifehealth.info/smh#23572414-6a80-4e0a-90b8-b79b96902ace"||	"HF03 -report"                              |
  |"http://smartlifehealth.info/smh#493e0727-8e57-4a86-924f-188d7cc67c8f"||	"HF04 -report"                                                        |
  |"http://smartlifehealth.info/smh#410357ab-4a13-40b4-907d-b3b478509cc3"||	"HF05 -report"                                                           |
  |"http://smartlifehealth.info/smh#4d4a12d2-4691-4b9f-af59-7ca5227f53a0"||	"HF06 -report"|
  |"http://smartlifehealth.info/smh#72b011ae-1c3f-4f89-9b78-23e8e06e30c8"||	"HF07 -report"|
  |"http://smartlifehealth.info/smh#82d2eef7-b8bb-4e10-bfc3-aa4eff9e4863"||	"DM01 on medication and not on register"|
  |"http://smartlifehealth.info/smh#016a2b1a-5a4a-43e7-886c-99b6731ca2b8"|                                                       |	"DM02 Coded as Diabetes Resolved"                                                             |
  |"http://smartlifehealth.info/smh#702571cb-b127-4d99-9be6-4aa65c95e5aa"||	"DM03 2 xIFCC grt than or eql 48 incl latest reading but not on diabetes register"                                 |
  |"http://smartlifehealth.info/smh#1b5d701b-d00e-41ba-b157-9030cedc1294"||	"DM04 IFCC gr than or eql 48 lat read and high fast or rand glu not on register"|
  |"http://smartlifehealth.info/smh#d42e3961-77f3-42dc-a920-50ceb411a2b4"||	"DM05 Patients with diabetes related code and not on register - latest high HbA1c"         |
  |"http://smartlifehealth.info/smh#8daf264e-7b3d-46e4-af08-a31f0e6099ff"|                                                     |	"DM06 IFCC gr than 48 latest reading and not found in any of the above search (2)"|
  |"http://smartlifehealth.info/smh#7ecb8b4c-317c-468f-91c2-afee483c2aff"||	"DM9001 - SGLT2_only"                    |
  |"http://smartlifehealth.info/smh#dab737d9-d72a-456e-9d6c-cad1ad5ccca6"|                     |	"DM00 - diabetes Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#eb823ac3-8643-41d5-9090-377534c5b24a"|   |	"ND01- Patients with Non-Diabetic Hypergllycaemia"                                                                  |
  |"http://smartlifehealth.info/smh#344fff8a-697d-4af7-92cc-cf56a53ad382"|                                                   |	"ND00- Non Diabetic HyperGlycaemia Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#bfd6eccc-a8c7-47dc-9527-2afa8dfac13d"|                                    |	"HF00 HF1-Heart Failure Register version 49 release 1"|
  |"http://smartlifehealth.info/smh#8c28ef6e-d8b2-49ac-874d-80572d1cc86f"|                                               |	"DM00 -report"          |
  |"http://smartlifehealth.info/smh#83b31bd7-e125-4963-8b39-2267b409b785"|                               |	"ND00 -report"                                        |
  |"http://smartlifehealth.info/smh#e2055d53-b59a-4201-9d2a-b96108bf9e22"|                                                         |	"DM01 -report"       |
  |"http://smartlifehealth.info/smh#5d56e7e2-8657-41de-8ef2-50a75af7e157"|                                                            |	"DM02 -report"                                       |
  |"http://smartlifehealth.info/smh#b7e90176-bce9-4218-857c-d524e69708ae"||	"DM03 -report"                     |
  |"http://smartlifehealth.info/smh#8cb3ee10-ee88-45ba-94ee-d2d4f8bff4de"||	"DM04 -report"|
  |"http://smartlifehealth.info/smh#f4afa1c6-080f-4d01-b6eb-b16425ca5fc6"||	"DM05 -report"                                 |
  |"http://smartlifehealth.info/smh#b28ba95c-5dad-4128-a2bb-69c65963a891"|                                 |	"DM06 -report"                             |
  |"http://smartlifehealth.info/smh#60d6e1f7-2276-4d04-8fee-4f2970714237"|                                         |	"ND01 -report"|
  |"http://smartlifehealth.info/smh#388e7805-4cb3-49ee-a03e-ca620cc89011"|                                          |	"HT01 Hypertension resolved code with ongoing HTN or on treatment for HTN"                                    |
  |"http://smartlifehealth.info/smh#7e729aec-da70-480e-8739-cf4a081b7b74"||	"HT02 HTN Diagnosis not Recognised by Qof"                                                               |
  |"http://smartlifehealth.info/smh#572fa7f3-2953-47da-b2a0-66eeaeccb4f0"|                                                                 |	"HT03 On HTN treatment codes and on treatment but not on register"                |
  |"http://smartlifehealth.info/smh#c5e629ef-d372-404f-9d2d-b338882fd875"|                                   |	"HT04 High BP Readings and on treatment but no diagnosis of HTN"|
  |"http://smartlifehealth.info/smh#bb9e56ef-4f21-45c9-aec1-46c6df334835"||	"HT05 High BP Readings and on treatment but no diagnosis of HTN - has diabetes"                  |
  |"http://smartlifehealth.info/smh#37b7673b-51f4-45bd-8673-e4cc39f9a3d0"||	"DM00 - diabetes Register Version 49 Release 1"                                                           |
  |"http://smartlifehealth.info/smh#9453805a-b747-489e-b2e5-bf7a05cfb640"||	"HT00 - Hypertension Register Version 49 Release 1"                            |
  |"http://smartlifehealth.info/smh#2cbf1415-3aec-4974-a3bf-7a535a682894"||	"HT00 -report"                                                     |
  |"http://smartlifehealth.info/smh#574ec582-210b-4306-9b9f-d81b0bc187dd"|                                              |	"HT01 -report"             |
  |"http://smartlifehealth.info/smh#bb9bd90b-bf16-4f02-a9cc-f99a4e2d25ad"|                                                                   |	"HT02 -report"                                   |
  |"http://smartlifehealth.info/smh#ffaf2d9a-eec6-4242-ac1c-3c19b374b19f"|                                                              |	"HT03 -report"|
  |"http://smartlifehealth.info/smh#be997e2d-e4fc-4071-ba61-e441244c39c0"|                                                                   |	"HT04 -report"|
  |"http://smartlifehealth.info/smh#4f6d4cba-c43e-4730-b1e1-4fff59ddc090"|                                                                  |	"HT05 -report"|
  |"http://smartlifehealth.info/smh#502fbaab-365a-4fe9-ba34-4e7612681a06"|                                                                   |	"LD01 Patients with learning disability codes"|
  |"http://smartlifehealth.info/smh#29721c58-5e55-41ce-9532-1d2ee6e23381"|                                |	"LD02 Patients with codes for learning disability not recognised by QoF"                      |
  |"http://smartlifehealth.info/smh#2874f425-2b03-4066-b348-a3b296bd53e4"|                                                                 |	"LD00 - Patients on the learning disabilities register version49 Release 1"|
  |"http://smartlifehealth.info/smh#5b998c53-75ed-41d3-bd9d-29610a282f97"|                                                               |	"LD00 -report"|
  |"http://smartlifehealth.info/smh#dd566b2b-8769-47c5-bc62-3e57cc8ca21b"|                                   |	"LD01 -report"|
  |"http://smartlifehealth.info/smh#a85a4cf6-3422-4748-afe4-30641ca00eac"||	"LD02 -report"|
  |"http://smartlifehealth.info/smh#d0294c6e-3720-4852-a3cc-fe305cfaf795"|                  |	"OB01 - not on register but got weight measurement"|
  |"http://smartlifehealth.info/smh#5108e317-9a7f-4f73-886f-770cbecd2515"|                                                  |	"OB02 - not on register but got confirmed obsesity code"                  |
  |"http://smartlifehealth.info/smh#4a611243-ff70-4e77-a926-f19fd592d8c1"|                                             |	"OB03 - not on register and needs both height and weight"|
  |"http://smartlifehealth.info/smh#567994ff-77fb-4dba-a190-eaf22dcf747f"|                                            |	"OB9001 - Known Obesity but not in the past year"|
  |"http://smartlifehealth.info/smh#95fda8ec-3acd-4683-884c-e9f2a5ac3726"|                                                    |	"OB00 - Obesity Register Version 49 Release 1"                                                                     |
  |"http://smartlifehealth.info/smh#8d0e8222-4514-4b53-9d51-015a4ddcec31"|                                                       |	"OB00 -report"               |
  |"http://smartlifehealth.info/smh#df67657a-ea1d-4aaf-af55-c4d16d29df4a"||	"OB01 -report"                                  |
  |"http://smartlifehealth.info/smh#6d05c330-12e3-439f-bbf2-5fb43e7c19d3"|                                                                  |	"OB02 -report"                                                        |
  |"http://smartlifehealth.info/smh#971bdba0-b871-45a0-8540-cc151dc2e94d"|                                                      |	"OB03 -report"              |
  |"http://smartlifehealth.info/smh#c631d4b7-74ce-4924-870b-923f28752643"||	"OS01 Age >= 75yrs - has osteoporosis code but no fragility fracture code"                                                              |
  |"http://smartlifehealth.info/smh#9b91693d-d2a1-4abf-9a49-df4741511e0b"|                                             |	"OS02 Age >= 75yrs - has fragility fracture code - no osteoporosis code"|
  |"http://smartlifehealth.info/smh#04358db0-2a39-42c6-a9c2-82ba7b5a714c"||	"OS03 Age >= 75yrs -no osteoporosis code - no fragility fracture code"                                   |
  |"http://smartlifehealth.info/smh#f3bf8676-3a0b-45e1-ad8c-d0c06fdce324"||	"OS04 Age 50-74yrs - has osteoporosis, +ve dexa, no fragility fracture code"     |
  |"http://smartlifehealth.info/smh#f8d418fa-ec1a-4567-9f4a-120e5868e389"|         |	"OS05 Age 50-74yrs - has fragility fracture - has +ve dexa scan - no osteoporosis"|
  |"http://smartlifehealth.info/smh#a6110177-3999-400c-95bf-fff82c355b1b"||	"OS06 Age 50-74yrs - positive dexa - no fragility fracture - no osteoporosis"                   |
  |"http://smartlifehealth.info/smh#025af10f-a8e6-47e6-85ca-cf0bc8d0a2a0"|                                          |	"OS07 Age 50-74yrs - has fragility fracture code - has osteoporosis -no dexa scan"|
  |"http://smartlifehealth.info/smh#7b5c8fa3-61da-4e78-bd3a-f1301a5e23ea"|                                                         |	"OS08 Age 50 - 74 years- Has osteoporosis - no fragility fracture - no DEXA scan"|
  |"http://smartlifehealth.info/smh#36713492-44a7-4175-96ed-289e44fc463e"|                    |	"OS09 Age 50-74yrs - has fragility fracture code - no dexa scan - no osteoporosis"|
  |"http://smartlifehealth.info/smh#fc3306a9-0111-4c5d-9c79-5851991f25c1"|                                                                |	"OS10 Age 50-74yrs - no fragility fracture code, no osteoporosis, no dexa scan"                  |
  |"http://smartlifehealth.info/smh#fa212de7-07c6-4ec9-92ec-f7a3b02e4fbf"||	"OS11 Medications suggestive of osteoporosis"|
  |"http://smartlifehealth.info/smh#755265a8-72d2-40b8-97a5-c30677fab2b1"|                                         |	"OS9001- age >= 75"        |
  |"http://smartlifehealth.info/smh#6ebec093-e095-469a-a01f-d70dcd31c5f8"||	"OS9002 - Fracture after 1-4-2012"                       |
  |"http://smartlifehealth.info/smh#7ce6849e-1403-461a-81f3-0254182d24b3"|         |	"OS9003 -Age between 50 and 74"  |
  |"http://smartlifehealth.info/smh#7f446c71-976a-4f75-b571-4c5ceb7417aa"|                |	"OS00 - Osteoporsis Register Version 49 Release 1"             |
  |"http://smartlifehealth.info/smh#dd5b403c-56c8-43b3-8213-58948923b691"||	"OS00 -report"                                                                      |
  |"http://smartlifehealth.info/smh#79e755be-af28-4637-b22d-ca129995091d"|                              |	"OS9004 - Fracture after 1-4-2014"                  |
  |"http://smartlifehealth.info/smh#02ef1ad5-e89a-4914-8388-6f1cbd3ce5e6"|             |	"OS01 -report"                                                           |
  |"http://smartlifehealth.info/smh#97d0cece-7737-4f5d-b790-3d11b7c29a99"|                                   |	"OS02 -report"                         |
  |"http://smartlifehealth.info/smh#639e6654-1b7e-4eca-ae37-33dfe00c4226"|                                   |	"OS03 -report"|
  |"http://smartlifehealth.info/smh#d56fd25d-9573-4736-bd41-9b71523bd5ed"|                                 |	"OS04 -report"|
  |"http://smartlifehealth.info/smh#ea4682ef-50b6-47c8-9e41-40694978783d"|                                   |	"OS05 -report"       |
  |"http://smartlifehealth.info/smh#afe583f4-f78b-4b1f-ac4d-906ac144f2cd"|                                  |	"OS06 -report"               |
  |"http://smartlifehealth.info/smh#f9417b36-ca50-417f-9139-a003e995c7f6"|                                  |	"OS07 -report"|
  |"http://smartlifehealth.info/smh#7f664aea-ea56-47d6-ab37-2965a7681f97"|                                 |	"OS08 -report"                                                               |
  |"http://smartlifehealth.info/smh#03aa6fa1-60d9-47a2-a80a-9393c42b080a"|                                   |	"OS09 -report"|
  |"http://smartlifehealth.info/smh#76f3253b-ff1d-466a-a6a0-71a45e779652"|                                                      |	"OS10 -report"|
  |"http://smartlifehealth.info/smh#8b46d297-cebf-4c8a-be24-6334ff1e3640"|                |	"OS11 -report"   |
  |"http://smartlifehealth.info/smh#e03dd2fa-5da5-4b49-92f1-55229071545e"||	"PC01 On Palliative Care Medication, may be suitable for palliative care register"|
  |"http://smartlifehealth.info/smh#a1b03a6e-8a45-4bce-a58c-b3ac56bda965"|               |	"PC00 - Patients on the palliative care register Version 49 Release1"|
  |"http://smartlifehealth.info/smh#e53c9234-edfc-4813-a555-c06e3621832e"||	"PC00 -report"                          |
  |"http://smartlifehealth.info/smh#76d70bde-e4ff-4b88-b9cf-d941a4648ede"||	"PC01 -report"                                  |
  |"http://smartlifehealth.info/smh#cd24908b-a02a-4e7b-8ca5-951272581393"|      |	"RA01 Patients with high antiCCP"           |
  |"http://smartlifehealth.info/smh#20c94d12-7a9b-4f88-b0b9-916f849ad629"|            |	"RA02 Patients with Rheumatology related code but not on RA register"|
  |"http://smartlifehealth.info/smh#f49a2f9c-2a1f-49c2-8602-aade14aa9d76"||	"RA00 - Rheumatoid Arthritis Register Version 49 Release 1"                                       |
  |"http://smartlifehealth.info/smh#6d704a3a-cc2b-4150-9556-7aadd8367c40"||	"RA00 -report"                |
  |"http://smartlifehealth.info/smh#dd41d941-9aa7-4924-8763-948f54ee4fef"||	"RA01 -report"|
  |"http://smartlifehealth.info/smh#59ccd7ac-a128-478f-8d0a-503960395a5f"||	"RA02 -report"|
  |"http://smartlifehealth.info/smh#af6463f3-a4af-49b9-980c-ba0876e9e582"||	"ST01 Patients with TIA or Strokes code indicative of dx but not on register"|
  |"http://smartlifehealth.info/smh#edd11b89-5e31-4542-9d16-4563b871d7f3"||	"ST02 Referral to stroke / TIA clinic"|
  |"http://smartlifehealth.info/smh#ad0593b5-0d8e-4630-8ef8-5fa95378661c"|            |	"ST00 - Stroke or TIA Register Version 49 Release 1"                               |
  |"http://smartlifehealth.info/smh#d288c78f-234b-44c5-be31-c1af3c3d4fe2"|                            |	"ST00 -report"                                                            |
  |"http://smartlifehealth.info/smh#c8ac46e0-f84a-4987-989e-a47d62b0d24d"|                                                          |	"ST01 -report"                  |
  |"http://smartlifehealth.info/smh#6741b8f3-7f2d-4566-96e0-ee785baf6dfa"|                                                                  |	"ST02 -report"|
  |"http://smartlifehealth.info/smh#28d8ecec-8a0e-4efa-a7a5-b8745a4a28df"|                                                        |	"PD01 Patients with low ABPI"                                                         |
  |"http://smartlifehealth.info/smh#f5aacb65-86a2-471e-ba6e-e956d3a1a432"|                                       |	"PD02 Patients with Codes Suggestive of PVD"|
  |"http://smartlifehealth.info/smh#c44ac9d6-d6be-4b51-ad46-75f543bac617"|                      |	"PD00 - Peripheral Vasular Disease Register Version 49 Release 1"|
  |"http://smartlifehealth.info/smh#14a14d31-0e47-4f54-a5fe-e36e8e19ae71"|                                                             |	"PD00 -report"|
  |"http://smartlifehealth.info/smh#4c2609c6-e4ea-431a-93dd-5a173ee75dd3"|                    |	"PD01 -report"                                            |
  |"http://smartlifehealth.info/smh#3e520df4-0e38-4c48-81fd-ea22572f253b"|   |	"PD02 -report"|
  |"http://smartlifehealth.info/smh#da17862e-c681-4912-8ca3-1ce476ac0524"|                                                      |	"16. Upload 1of1 NWL RESP 8KC v2.0.250615 -report"            |
  |"http://smartlifehealth.info/smh#aadaf465-a874-4fef-b640-a8b1014f0995"|                |	"07. Upload 1of1 NWL NDH 01 v2.0.250529 -report"   |
  |"http://smartlifehealth.info/smh#2bcca615-76f1-4af6-a223-8607b5e1c60e"||	"13. Upload 1of1 NWL HTN 02 v2.0.250429 -report"                                |
  |"http://smartlifehealth.info/smh#c0f6fa14-af67-48ba-934e-45168651ee6b"|                                                                |	"12. Upload 1of1 NWL HTN 01 v2.0.250429 -report"|
  |"http://smartlifehealth.info/smh#b2312e00-9431-4da6-94ef-a4c83b0c03cb"||	"11. Upload 1of1 NWL CKD 02 v2.0.250518 -report"                                        |
  |"http://smartlifehealth.info/smh#b364a91e-0650-4167-89a9-38eeef11fc23"||	"02. Upload 1of1 NWL DML1 3TT v2.0.250505 -report"                     |
  |"http://smartlifehealth.info/smh#6388cd33-5e06-4c24-b708-7aebe05db4bb"|     |	"01. Upload 1of1 NWL DML1 9KC v2.0.250526 -report"|
  |"http://smartlifehealth.info/smh#b53b2511-9eaf-40d4-9c3f-465a5aceff0b"||	"10. Upload 1of1 NWL CKD 01 v2.0.250518 -report"                                                            |
  |"http://smartlifehealth.info/smh#10a1eb13-925f-4d10-99cf-9fc7afbbb197"|                                                              |	"03. Upload 1of1 NWL DML1 nDM v2.0.250505 -report"                                                   |
  |"http://smartlifehealth.info/smh#47ef227c-1dc0-4b38-87b5-6cf1c0b78516"||	"05. Upload 1of1 NWL DML1 CP v2.0.250518 -report"|
  |"http://smartlifehealth.info/smh#370f8d1d-4150-48fb-86f1-b0d51ce64960"||	"04. Upload 1of1 NWL DML1 MH v2.0.250518 -report"                                                        |
  |"http://smartlifehealth.info/smh#b704816d-76bd-4e38-b3e9-2133d4ed7dcc"|   |	"09. Upload 1of1 NWL NDH AR v2.0.250526 -report"|
  |"http://smartlifehealth.info/smh#02302a24-5e8c-4edf-a508-4b1ad7964cce"||	"08. Upload 1of1 NWL NDH NDPP v2.0.250505 -report"                                       |
  |"http://smartlifehealth.info/smh#d42f3eb7-3238-44dc-ada8-ef62405f6077"|                                                              |	"NDH_REG Register - Aged 18 or over with non-diabetic hyperglycaemia"|
  |"http://smartlifehealth.info/smh#d5d5e1c1-96b8-4a38-bcb5-92781af9b750"||	"unresolved diabetes diagnosis"|
  |"http://smartlifehealth.info/smh#70c4a14c-2be8-429e-90e8-b61167392a86"||	"No diabetes diagnosis or Patient's latest diabetes diagnosis followed by resolved code"                                                                      |
  |"http://smartlifehealth.info/smh#58b4392d-82d4-4c93-804e-f4cafd9bafbf"|                                                                  |	"Latest BP outside range"|
  |"http://smartlifehealth.info/smh#a38c533a-5ab1-4375-955b-417bdcbb8ce6"||	"More than 1 BP outside range"                      |
  |"http://smartlifehealth.info/smh#27c05495-800e-4d04-88a1-80421c463eed"|                                                                     |	"1. Indication of PVD but not on register"|
  |"http://smartlifehealth.info/smh#7e893a91-3b12-4a4b-8d1a-1d414eae9579"|                                                                     |	"2. Patients with evidence of PVD"|
  |"http://smartlifehealth.info/smh#f9208762-fd20-4332-9e21-f2dd507c51fe"|                                             |	"1. Indication of PVD but not on register (2)"                                                |
  |"http://smartlifehealth.info/smh#6059f612-095d-4545-b115-94737990cf6c"|     |	"PVD medicaiton - vasodilators"      |
  |"http://smartlifehealth.info/smh#37f03b8c-947a-4693-8e3b-2ca3913ac14c"|      |	"PVD medication - Aspirin and clopidogrel combination"                               |
  |"http://smartlifehealth.info/smh#2574336e-f2d2-4c1e-9032-2c9430df1713"||	"PVD medications - oral anticoagulants and not DVT"       |
  |"http://smartlifehealth.info/smh#266f90c7-7896-4088-8a98-94bcce8549d1"|                                                      |	"PVD Register"                                    |
  |"http://smartlifehealth.info/smh#3f524680-7813-4443-ab10-c21e282a0847"|  |	"PD02 -report"                                                  |
  |"http://smartlifehealth.info/smh#f4bb8de4-7cd9-4b4d-bf90-5431538ec02c"|                                     |	"ABPM01-ES-24 hr Blood Pressure Monitoring WITHOUT Enhanced Services Admin Code"    |
  |"http://smartlifehealth.info/smh#a1ed2eb9-08b8-4c9b-95ba-5544b432567f"|                                                                  |	"ABPM02-ES-Housebound Patients with ABPM WITHOUT Home Visit"|
  |"http://smartlifehealth.info/smh#07052693-4f02-4eea-85dc-3deb2bed42f2"|                                                                 |	"Anonymised Identifer- DQ Report- POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#852b7757-9add-4ae1-be5c-65518e63024d"|                                                             |	"NHS Numbers - DQ Report -POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#d84094a1-ef19-4005-ad5b-a37bcb923fe3"||	"Anonymised Identifier - Data Quality Report - missing POTENTIAL Home Visit Code"                                                               |
  |"http://smartlifehealth.info/smh#80bc8ac4-21db-49bc-a0ad-ba3273b6a1fd"|        |	"NHS Numbers - Data Quality Report - missing POTENTIAL Home Visit Code"                                                 |
  |"http://smartlifehealth.info/smh#9f1fa91c-20c5-447e-8333-31be473cd527"|                                                      |	"ABPM01-ES-PAYMENT-Consultations for 24 hr Blood Pressure Monitoring"                                                       |
  |"http://smartlifehealth.info/smh#fcb94187-ad3e-4274-bb66-ce6c6a695a9b"|     |	"ABPM02-ES-PAYMENT-Home Visits for 24 hr Blood Pressure Monitoring"|
  |"http://smartlifehealth.info/smh#df5e4cfb-e177-49f8-b789-bf0a8d9ed22c"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#a21762fc-cd2b-400c-8fa7-819a16e7f482"||	"EMABPM01 -report"                                                               |
  |"http://smartlifehealth.info/smh#e8247983-ce35-4e69-82d2-81e3a5b485bb"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#e4bd83b8-d9c7-425d-8ba7-a025a5961f21"||	"EMABPM02 -report"|
  |"http://smartlifehealth.info/smh#3df4615e-0cca-4cb4-b048-6c7abd908b58"||	"ABPM01-ES-PAYMENT-Consultations for 24 hr Blood Pressure Monitoring"|
  |"http://smartlifehealth.info/smh#250333bc-1bb9-434f-a7c1-2d4ca699c991"|                                                      |	"ABPM02-ES-PAYMENT-Home Visits for 24 hr Blood Pressure Monitoring"|
  |"http://smartlifehealth.info/smh#ff22c2ca-8953-40ff-9bdb-0157c726eaf5"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#145b44da-2317-4bf4-88ae-a497c03f6e68"||	"EMABPM01 -report"|
  |"http://smartlifehealth.info/smh#da999829-4d6e-4ae0-8e53-db8ec0c5f3f3"|                                            |	"Activity Level Report for Payment"                                             |
  |"http://smartlifehealth.info/smh#4b476ef6-d10b-4cd8-9c2a-976a178924de"|                                        |	"EMABPM02 -report"                |
  |"http://smartlifehealth.info/smh#5b6d1fb2-7974-4131-8ba7-19e9605f73e5"|                                                 |	"ABPM01-ES-PAYMENT-Consultations for 24 hr Blood Pressure Monitoring"|
  |"http://smartlifehealth.info/smh#fc042fa8-7941-4de7-a887-6b737ff9e61e"|      |	"ABPM02-ES-PAYMENT-Home Visits for 24 hr Blood Pressure Monitoring"                                                  |
  |"http://smartlifehealth.info/smh#b7a34095-e382-46db-8256-15cc514c8206"|       |	"Activity Level Report for Payment"        |
  |"http://smartlifehealth.info/smh#855c3e3e-b892-4055-a0d9-b88e8417176e"|                             |	"EMABPM01 -report"|
  |"http://smartlifehealth.info/smh#5be37451-a04c-4325-9774-5f5b236e9a93"|                                                               |	"Activity Level Report for Payment"                          |
  |"http://smartlifehealth.info/smh#a51864af-e757-4525-9648-9a2c78a9cd2c"|                          |	"EMABPM02 -report"                                        |
  |"http://smartlifehealth.info/smh#4e9fc098-adc1-426b-b210-53582df2f00f"|                                                         |	"ABPM01-ES-PAYMENT-Consultations for 24 hr Blood Pressure Monitoring"|
  |"http://smartlifehealth.info/smh#e03e5c51-8480-4d82-8f99-141ca89d141f"|       |	"ABPM02-ES-PAYMENT-Home Visits for 24 hr Blood Pressure Monitoring"       |
  |"http://smartlifehealth.info/smh#de3898d1-5b55-4854-8c80-8d07a03d2459"||	"Activity Level Report for Payment"                                        |
  |"http://smartlifehealth.info/smh#0c9845a0-f886-4368-a002-96adc9f2982d"|                         |	"EMABPM01 -report"|
  |"http://smartlifehealth.info/smh#6507bb2a-adeb-432f-89cd-76323052cdcf"|                                                         |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#6cbbaa94-2c97-4946-8455-38bb593ecf88"|                                         |	"EMABPM02 -report"|
  |"http://smartlifehealth.info/smh#cc8d6538-1fe5-4c04-bca0-3f504fac6a4f"|                                    |	"ABPM01-ES-PAYMENT-Consultations for 24 hr Blood Pressure Monitoring"|
  |"http://smartlifehealth.info/smh#e9728e37-9893-4865-8f61-9c771cef17bb"||	"ABPM02-ES-PAYMENT-Home Visits for 24 hr Blood Pressure Monitoring"|
  |"http://smartlifehealth.info/smh#d675dc88-ce09-419c-b201-5af4ad2afb60"||	"ABPM01a - Female or Unknown"            |
  |"http://smartlifehealth.info/smh#ee80cfd2-7aa8-4004-bae2-c4986f9d4602"||	"ABPM01b - Male"|
  |"http://smartlifehealth.info/smh#e9d78a5d-67b9-4f8c-8eeb-7de7cab0c1c3"|           |	"EMABPM01 -report"|
  |"http://smartlifehealth.info/smh#0a77cf9c-3415-421c-a0a1-ff4c6595d8a7"||	"EMABPM02 -report"                                         |
  |"http://smartlifehealth.info/smh#7ef9b74a-fa44-4596-8729-e2fb12094eaa"|             |	"EMABPM01a -report"|
  |"http://smartlifehealth.info/smh#30c2dd4c-5776-4d02-b370-583da3e817cf"||	"EMABPM01b -report"        |
  |"http://smartlifehealth.info/smh#a16e1b4d-10be-410a-9e56-f5c2296a4097"|           |	"Access  01/04/25 TO END THIS FY  High risk cohort requiring continuity of care"                           |
  |"http://smartlifehealth.info/smh#ba877897-fac4-4904-be05-d756d53cc7ac"|                                            |	"ACS01  PAYMENT  Clinical encounter or consultation"|
  |"http://smartlifehealth.info/smh#b64e368b-77ad-40a5-a7a8-a0fa94f63bbe"||	"ACS02  PAYMENT  MDT meeting activity"                                                        |
  |"http://smartlifehealth.info/smh#0567fae1-049a-4f9f-b899-dafb952f4408"||	"Activity Level Report for Payment"                                         |
  |"http://smartlifehealth.info/smh#69e882ea-77cb-4106-bbe7-3750a99566f3"||	"EMACS01 -report"                                                           |
  |"http://smartlifehealth.info/smh#9452c7bc-0d50-4667-bd22-58ae31df0a62"|                                 |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#a32e6f55-96c5-4aa3-96e1-a5686f1af63d"|                 |	"EMACS02 -report"    |
  |"http://smartlifehealth.info/smh#e8396f34-ea48-4f38-b3ff-49d9808cbe40"|                 |	"ACS01  PAYMENT  Clinical encounter or consultation"                                                  |
  |"http://smartlifehealth.info/smh#517f6afa-9bca-4832-bc6e-7334382721e4"||	"ACS02  PAYMENT  MDT meeting activity"|
  |"http://smartlifehealth.info/smh#58f4a73a-9de5-47a7-9f5e-cc914cfc3518"|                                                 |	"Activity Level Report for Payment"                                       |
  |"http://smartlifehealth.info/smh#b30ad9fe-b5a8-4cd7-8954-e71bcd237bc4"||	"EMACS01 -report"                 |
  |"http://smartlifehealth.info/smh#e0f3ff74-454e-4a15-9ad5-5a561c1327cf"||	"Activity Level Report for Payment"                                              |
  |"http://smartlifehealth.info/smh#50b23398-26cb-4cb6-8a00-d878bab4ca2d"||	"EMACS02 -report"                      |
  |"http://smartlifehealth.info/smh#973e275a-cce0-4f0a-bd3e-b0a157e46cee"||	"ACS01  PAYMENT  Clinical encounter or consultation"|
  |"http://smartlifehealth.info/smh#e493611d-0bc9-406d-a165-cb8844b6e6c7"||	"ACS02  PAYMENT  MDT meeting activity"|
  |"http://smartlifehealth.info/smh#f6c874f2-fe7c-4b8d-b514-29ce3f357f15"|          |	"Activity Level Report for Payment"                                      |
  |"http://smartlifehealth.info/smh#1a5e5f71-f66e-4da2-acf0-36ca104596e2"||	"EMACS01 -report"                                               |
  |"http://smartlifehealth.info/smh#57b695b1-8f4c-4a8b-b1d1-ae0cb26b4ac3"||	"Activity Level Report for Payment"                   |
  |"http://smartlifehealth.info/smh#7937ba44-ca59-4829-a7a6-88afe4f9ac16"||	"EMACS02 -report"                                   |
  |"http://smartlifehealth.info/smh#44550a6f-9536-4ef1-9e23-587fd5dcddfc"||	"ACS03  PAYMENT  APR-25 - END THIS FY  High risk cohort continuity of care"                                                                 |
  |"http://smartlifehealth.info/smh#4e08f304-3c33-4de5-bfc5-3bcd29ad062e"||	"ACS01  PAYMENT  Clinical encounter or consultation"|
  |"http://smartlifehealth.info/smh#55c91be5-ae93-44d1-8894-10ee5a05f864"|                                             |	"ACS02  PAYMENT  MDT meeting activity"|
  |"http://smartlifehealth.info/smh#3e86509d-0891-4fac-8979-113d5224b2da"|              |	"EMACS03 -report"                                                     |
  |"http://smartlifehealth.info/smh#a0b56fe1-0dda-4ef4-a4b4-87f84919ad44"|                 |	"Patient Level Report for Payment"                                              |
  |"http://smartlifehealth.info/smh#d82bc92a-022a-46ab-8632-3a55191e3807"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#d2de75f4-6dac-4583-98a6-e02814cd4633"||	"EMACS01 -report"                            |
  |"http://smartlifehealth.info/smh#23e4103e-29f0-445e-920c-48c7047b08aa"|         |	"Activity Level Report for Payment"                           |
  |"http://smartlifehealth.info/smh#f69f1d82-d05e-47d2-8be1-eadfa2d8e1d0"|                                                                    |	"EMACS02 -report"|
  |"http://smartlifehealth.info/smh#1fa71b10-8d1d-4aba-817d-2f238c2a45bf"||	"AC00-ES-International Normalised Ratio MISSING Enhanced Services Admin code"                                                                      |
  |"http://smartlifehealth.info/smh#a71ed87c-06ab-475e-bc3d-154e9712e167"|                                                           |	"AC01-ES-Warfarin Therapy Started WITHOUT International Normalised Ratio"               |
  |"http://smartlifehealth.info/smh#19277ab1-3ff2-4e70-b32b-7a26d4204d90"||	"AC02-ES-Housebound Patients on Warfarin Initiation MISSING Home Visit Code"                                                   |
  |"http://smartlifehealth.info/smh#bea1b675-7256-4b77-8bc2-8f3462cfb357"|                                                                |	"AC03-ES-Warfarin Monitoring WITHOUT International Normalised Ratio"                                 |
  |"http://smartlifehealth.info/smh#20a3bb59-092d-4f71-b4c9-7673aedde325"||	"AC04-ES-Housebound Patients on Warfarin Monitoring MISSING Home Visit Code"|
  |"http://smartlifehealth.info/smh#deca4810-a061-41f5-9344-a1577bb1c5c6"|                                                            |	"Anonymised Identifier-DQ Report-POTENTIAL Missing Enhanced Services Admin"                                                     |
  |"http://smartlifehealth.info/smh#ea5e4e3a-ed83-4c93-932e-d6ac3ac39a3d"||	"NHS Numbers-DQ Report-POTENTIAL Missing Enhanced Services Admin"                                                                     |
  |"http://smartlifehealth.info/smh#c579c68f-7a7f-4404-a80f-df326f985030"||	"Anonymised Identifier-DQ Report-POTENTIAL Missing International Normalised Ratio"|
  |"http://smartlifehealth.info/smh#d0f405c1-3b1e-4007-b46e-edc0a596c3c8"|                                              |	"NHS Numbers-DQ Report-Missing International Normalised Ratio"                                                             |
  |"http://smartlifehealth.info/smh#941a8251-df8c-4db2-9994-8c79373890c1"||	"Anonymised-DQ Report-POTENTIAL Missing Home Visit Code at same time as INR"                             |
  |"http://smartlifehealth.info/smh#775d7de2-b637-4888-bc4b-0d1fd62cc803"|                                                             |	"NHS Numbers-DQ Report-POTENTIAL Missing Home Visit Code at same time as INR"                                               |
  |"http://smartlifehealth.info/smh#8283c64b-5240-4f59-a6a6-9cca033bb17a"|                                                       |	"Anonymised - DQ Report - Missing International Nomalised Ratio"|
  |"http://smartlifehealth.info/smh#8240d6da-a52f-4b26-98a2-c433cf83be71"||	"NHS Numbers - DQ Report - Missing International Nomalised Ratio"|
  |"http://smartlifehealth.info/smh#1206e117-18de-48c8-8622-10f95fe34ad3"|                                                   |	"Anonymised-DQ Report-POTENTIAL Missing Home Visit Code at same time as INR"   |
  |"http://smartlifehealth.info/smh#2a28c068-6367-4d35-a9a2-9af9a0ca8e3b"||	"NHS Numbers-DQ Report-POTENTIAL Missing Home Visit Code at same time as INR"|
  |"http://smartlifehealth.info/smh#a467fafe-9c7e-4943-9093-8ae9369dcb21"|                      |	"AC01-ES-PAYMENT-Consultations for Warfarin Initiation"                                                        |
  |"http://smartlifehealth.info/smh#65131f53-5487-422a-a146-e57171e54d7d"||	"AC02-ES-PAYMENT-Home Visits for Warfarin Initiation"|
  |"http://smartlifehealth.info/smh#7dca552e-3e85-4bbf-b4a2-a19d08469e52"||	"AC03-ES-PAYMENT-Consultations for Warfarin Monitoring"|
  |"http://smartlifehealth.info/smh#b07a7c14-bbbe-4cf3-97cc-690d8c8e9077"|                                                                   |	"AC04-ES-PAYMENT-Home Visits for Warfarin Monitoring"                                     |
  |"http://smartlifehealth.info/smh#3d177bf1-e8c4-4c34-979d-1417b815a46d"||	"Activity Level Report for Payment"                                         |
  |"http://smartlifehealth.info/smh#1d659c34-9041-49ce-8046-8ae4eeaf9496"||	"EMAC01 -report"                                             |
  |"http://smartlifehealth.info/smh#4e6ab40c-4c50-4a50-ae10-fd2021abc911"||	"Activity Level Report for Payment"        |
  |"http://smartlifehealth.info/smh#d0f0cada-0eec-4709-b415-329b7a558db5"|                   |	"EMAC02 -report"                                            |
  |"http://smartlifehealth.info/smh#cbeb028c-fe0f-46e2-aa51-2daf73ad15f6"|                                               |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#4d271d2f-4661-464f-b633-b92399f61b27"|                        |	"EMAC03 -report"|
  |"http://smartlifehealth.info/smh#7d52b355-4096-4cb9-b78c-c974503c1a09"|  |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#f6e8dd69-6ce3-47d0-91f4-8a36dc190819"|                   |	"EMAC04 -report"                        |
  |"http://smartlifehealth.info/smh#870e60e6-28b3-4abb-8742-24c4a4ad3ca2"|                                                 |	"AC01-ES-PAYMENT-Consultations for Warfarin Initiation"                                               |
  |"http://smartlifehealth.info/smh#e7576a46-bfbb-4fc0-8fa2-bf65355be834"|            |	"AC02-ES-PAYMENT-Home Visits for Warfarin Initiation"                      |
  |"http://smartlifehealth.info/smh#93f38857-66f8-46ef-916a-2cbd7715a0fc"||	"AC03-ES-PAYMENT-Consultations for Warfarin Monitoring"                             |
  |"http://smartlifehealth.info/smh#f7d60c8f-843b-4017-b5a5-5d1d5333d19d"||	"AC04-ES-PAYMENT-Home Visits for Warfarin Monitoring"|
  |"http://smartlifehealth.info/smh#5e93fb1f-ba94-4b8e-bef8-1f39248bbd7e"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#4801c8b6-a8b1-454e-9039-e57f5b58733f"|             |	"EMAC01 -report"   |
  |"http://smartlifehealth.info/smh#b5bbfc07-48f9-4648-b699-7da67b819c0a"|  |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#99ac07fe-6232-4b44-bf4b-be1f29c64b3d"|               |	"EMAC02 -report"|
  |"http://smartlifehealth.info/smh#2c049b67-a506-4b8a-8af3-00f21aa2fde8"|                                                           |	"Activity Level Report for Payment"                                      |
  |"http://smartlifehealth.info/smh#02ef0490-7998-4ff7-bd22-0671494c5b4a"|                            |	"EMAC03 -report"|
  |"http://smartlifehealth.info/smh#ec285691-4074-42b2-a1bb-6e6ec65b9678"|  |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#de35a7cc-d953-45aa-9971-2813a29d3822"|         |	"EMAC04 -report"   |
  |"http://smartlifehealth.info/smh#d4f94eab-d17a-47e4-b832-9d3e588dbff0"|  |	"AC01-ES-PAYMENT-Consultations for Warfarin Initiation"             |
  |"http://smartlifehealth.info/smh#1a27b6cc-ae11-4f97-96b9-16c0e01834b0"|                   |	"AC02-ES-PAYMENT-Home Visits for Warfarin Initiation"|
  |"http://smartlifehealth.info/smh#efcc8ba0-eb22-452d-b856-469fc1f8003b"||	"Activity Level Report for Payment"                        |
  |"http://smartlifehealth.info/smh#e1275100-1891-4bd4-8397-f2ab298003a4"|                                                                     |	"EMAC01 -report"|
  |"http://smartlifehealth.info/smh#c43e86d2-53f2-4fbc-aab9-b3b9522bfe95"|  |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#b5a6da30-f448-4270-ac3e-6d9d22b631c7"||	"EMAC02 -report"                   |
  |"http://smartlifehealth.info/smh#ca7d1828-a087-4485-97a7-e9e562a4a621"||	"AC03-ES-PAYMENT-Consultations for Warfarin Monitoring"                                        |
  |"http://smartlifehealth.info/smh#a1178af9-6b0d-4e87-ac18-183305d63678"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#127c68ef-8576-4e57-99f1-85393f57c205"||	"EMAC03 -report"|
  |"http://smartlifehealth.info/smh#b6177a1f-a0e6-406a-9804-d6f4ac57679b"| |	"AC04-ES-PAYMENT-Home Visits for Warfarin Monitoring"|
  |"http://smartlifehealth.info/smh#fbde597e-d434-4b79-a90c-ef7932312786"||	"Activity Level Report for Payment"                                                         |
  |"http://smartlifehealth.info/smh#870b305c-e885-4987-8e4b-a8376363e729"||	"EMAC04 -report"|
  |"http://smartlifehealth.info/smh#860fdba0-fbec-4b5f-a791-e17222d1a0d0"||	"AC03-ES-PAYMENT-Consultations for Warfarin Monitoring"|
  |"http://smartlifehealth.info/smh#5d89d361-0f03-4870-9a78-a40275416049"||	"AC03a - Female or Unknown"                                        |
  |"http://smartlifehealth.info/smh#f7ef8575-90ea-4bf7-b4d6-baf27b7dc39b"||	"AC03b - Male"          |
  |"http://smartlifehealth.info/smh#1bb0d0d1-edef-4ced-838a-3baf67f53952"|                       |	"EMAC03 -report"|
  |"http://smartlifehealth.info/smh#9372b7f3-f276-46cd-9185-1d4bca710a76"|  |	"EMAC03a -report"|
  |"http://smartlifehealth.info/smh#26265596-34fa-43a4-b316-1707ed92e2eb"||	"EMAC03b -report"                          |
  |"http://smartlifehealth.info/smh#2baf5a5a-98d0-48ed-a761-e3add318b8bf"|                                          |	"AC03-Q1-Consultations for Warfarin Monitoring"                        |
  |"http://smartlifehealth.info/smh#5fcccf19-7223-4359-8538-9eecba50e81a"|                       |	"AC03-Q2-Consultations for Warfarin Monitoring"|
  |"http://smartlifehealth.info/smh#0bf46a13-dbb7-46a7-9380-1094beb595b2"|                             |	"AC03-Q3-Consultations for Warfarin Monitoring"                                                                    |
  |"http://smartlifehealth.info/smh#85cef068-1335-4a6c-a6ab-9db1fc143b78"|                   |	"AC03-Q4-Consultations for Warfarin Monitoring"                           |
  |"http://smartlifehealth.info/smh#4020b7d8-7d58-4b6d-a184-76193b558dab"|                       |	"AS01D-ES-Patients registered as an Asylum Seeker"                                                         |
  |"http://smartlifehealth.info/smh#4e378da7-7768-4db7-8634-4cccecaabad9"|                |	"AS02Na-ES-Patients with Height recorded"                 |
  |"http://smartlifehealth.info/smh#ae4c5474-7b8e-4cf4-96f6-d80a918aa0cf"|                      |	"AS02Nb-ES-Patients with Weight recorded"|
  |"http://smartlifehealth.info/smh#d6cd6969-197d-4a9f-8dad-7152e43b578d"|                      |	"AS02Nc-ES-Patients with BMI recorded"                                                     |
  |"http://smartlifehealth.info/smh#f672134d-23d2-41a1-91cf-e876e9e79048"|     |	"AS02Nd-ES-Patients with Blood pressure recorded"|
  |"http://smartlifehealth.info/smh#a7e7cf46-b59a-4146-a1ed-6c8be3beaa75"|              |	"AS02Ne-ES-Patients with Pulse rate or pulse rhythm recorded"|
  |"http://smartlifehealth.info/smh#3bf02a49-fc4d-4629-a53c-a0d850be2e93"||	"AS02Nf-ES-Patients with Smoking status recorded"                        |
  |"http://smartlifehealth.info/smh#de74fa6b-f3b3-4176-ad9d-8ddf2982299a"||	"EMAS02N -report"|
  |"http://smartlifehealth.info/smh#725d357c-914b-43e6-af8d-740c79c6a2b3"||	"Health Assessment - NHS NUMBERS - Checklist -report"             |
  |"http://smartlifehealth.info/smh#32d351f5-b4a5-4b7e-a0f6-b0fb828a68bd"||	"Health Assessment - NHS NUMBERS - More Detailed -report"              |
  |"http://smartlifehealth.info/smh#235dfaba-239d-4a96-825a-4f1f0e8dace8"|     |	"AS03N-ES-Patients with Medication Review"                       |
  |"http://smartlifehealth.info/smh#5110fd78-8f40-4b9a-bb54-57026cdc0c86"|                 |	"EMAS03N -report"                                                                   |
  |"http://smartlifehealth.info/smh#73cd7384-460f-414e-8733-05e0a310fe82"|                                    |	"Medication Review - NHS NUMBERS -report"                                |
  |"http://smartlifehealth.info/smh#6a5a8c96-e12a-4573-9dc5-d5d0edcfaa1b"|            |	"AS04N-ES-Patients with Flu Immunisation recorded"                                                  |
  |"http://smartlifehealth.info/smh#330dea82-1e4b-4c97-9743-5b92ca2b442c"|             |	"EMAS04N -report"|
  |"http://smartlifehealth.info/smh#6c88a7a8-dd28-4d70-a80c-ec92156af8f4"|                                                      |	"Flu Immunisation - NHS NUMBERS -report"                                                         |
  |"http://smartlifehealth.info/smh#be77d2dd-4f85-45f6-ba30-9202a06f40da"|                       |	"AS05N-ES-Patients with Safeguarding recorded"                                  |
  |"http://smartlifehealth.info/smh#9442aef7-4406-430d-99b2-6cdbfb23b51d"||	"EMAS05N -report"                                                           |
  |"http://smartlifehealth.info/smh#6fb5044c-ee77-44bb-8e63-b547bf8194c7"|                |	"Safeguarding - NHS NUMBERS -report"   |
  |"http://smartlifehealth.info/smh#387827e4-c646-478b-8640-540b997371f6"|                          |	"AS06N-ES-Patients with Mental Health Assessment recorded"                             |
  |"http://smartlifehealth.info/smh#bfccf691-4ecc-41cb-a5cc-31200cbe1edf"|          |	"EMAS06N -report"                                   |
  |"http://smartlifehealth.info/smh#f2631c44-ed8a-4fb1-bbb4-7f6526a2d9c5"||	"Mental Health Assessment - NHS NUMBERS -report"|
  |"http://smartlifehealth.info/smh#70ca850e-994e-4088-a9a0-ececd79f08ac"|                      |	"AS07N-ES-Patients with Care Plan recorded"|
  |"http://smartlifehealth.info/smh#43b20888-553b-40e2-b6a3-fae8a290706e"|                                     |	"Care Plan - NHS NUMBERS -report"                      |
  |"http://smartlifehealth.info/smh#1a6bbfe7-2017-4f9d-b357-bcac76dbf5b4"|           |	"EMAS07N -report"                              |
  |"http://smartlifehealth.info/smh#f6cecde1-3411-450a-9ee0-9ef029e74dfc"||	"CRM01DD  HYP  DENOMINATOR  BP>=140/90 or Daytime Average BP>=135/85"                                                     |
  |"http://smartlifehealth.info/smh#a6b19e7b-1dd6-46e5-9b06-e28ebb5f9ccc"||	"CRM01BD  CKD  DENOMINATOR  Patients who are likely to have CKD"                    |
  |"http://smartlifehealth.info/smh#d0442de3-fdcb-437b-a231-e9c8af5fb49c"|                                                                     |	"CRM01CD  DM  DENOMINATOR  HbA1c>=48 and NOT on DM register"                            |
  |"http://smartlifehealth.info/smh#638ee87c-f95c-4ef1-ae3f-0b295e09dd43"||	"EMCRM01DD -report"|
  |"http://smartlifehealth.info/smh#5d9fbaa9-fe2d-41a0-b177-c45f3a768d73"||	"NHS NUMBERS  EMCRM01D  Hypertension Detection -report"|
  |"http://smartlifehealth.info/smh#63f30740-d635-47e3-a638-4306191519bb"|                                                       |	"CRM01AD  AF  DENOMINATOR  Eligible for ECG or Pulse Rhythm Check"                                                                    |
  |"http://smartlifehealth.info/smh#d6308e79-a436-4156-896e-7ee468cde4d6"||	"CRM01B  ACHIEVEMENT  Patients diagnosed and coded with CKD (in FY)"|
  |"http://smartlifehealth.info/smh#cfbe3c97-f7f7-45be-9353-941d73a69542"|                          |	"EMCRM01B -report"|
  |"http://smartlifehealth.info/smh#af8c2b73-80d2-450d-b157-b456195a0b55"|                             |	"NHS NUMBERS  EMCRM01B  CKD Detection -report"                        |
  |"http://smartlifehealth.info/smh#18980740-cb7b-4aca-bb6c-ca29997da6c0"|             |	"CRM01ED  NDH  DENOMINATOR  HbA1c>=42 & < 48 AND NO DM or NDH Diagnosis"                        |
  |"http://smartlifehealth.info/smh#6004ae89-b49f-4b5e-b992-9fccfe59e962"|   |	"CRM01C  DM  ACHIEVEMENT  DM(HbA1c>=48) OR NDH(HbA1c btwn 42&47) or HbA1c<42"                                                      |
  |"http://smartlifehealth.info/smh#effbe4a6-02bc-448a-9d5d-aeb342c4acec"||	"EMCRM01CD -report"|
  |"http://smartlifehealth.info/smh#5804dfcf-ebfe-47e5-b5db-11bf8f2be573"|          |	"NHS NUMBERS  EMCRM01C  Diabetes detection -report"                             |
  |"http://smartlifehealth.info/smh#43295c05-ac5e-4ff7-a7c2-4dcc6f3e28ad"|                    |	"CRM01D  HYP  ACHIEVEMENT  BP>=140/90 OR Daytime Avg BP=135/85 AND HYP Diag"                                               |
  |"http://smartlifehealth.info/smh#4d73bb78-fa47-42ed-9600-04e1d274ee6b"|                                                      |	"CRM01A  AF  ACHIEVEMENT  THIS FY  ECG or Pulse Rhythm recorded"                                         |
  |"http://smartlifehealth.info/smh#0195ba47-4cdc-46ab-ad1d-796dc724138a"||	"EMCRM01AD -report"                                                                      |
  |"http://smartlifehealth.info/smh#263b4594-daff-4ea6-9dd4-7e328d8c9b01"||	"NHS NUMBERS  EMCRM01A  AF screening -report"|
  |"http://smartlifehealth.info/smh#1a2f556e-2138-4fc8-b355-9a758db27155"||	"EMCRM01BN -report"                      |
  |"http://smartlifehealth.info/smh#57a3a54d-1dfd-4209-a5f5-71aea8ec091f"|               |	"CRM01E  NDH  ACHIEVEMENT  NDH(HbA1c>=42&<48) OR DM(HbA1c>=48) OR HbA1c<42"                                                   |
  |"http://smartlifehealth.info/smh#bc9b4822-7b2e-4b98-81ba-63ce0d26f4f1"|                                                      |	"EMCRM01ED -report"                                                |
  |"http://smartlifehealth.info/smh#4a16e2db-23d0-4a07-8eec-80044d698457"|                |	"NHS NUMBERS  EMCRM01ED  NDH Detection -report"|
  |"http://smartlifehealth.info/smh#53a99a57-b05c-4885-9594-a45505723732"|                               |	"EMCRM01CN -report"                     |
  |"http://smartlifehealth.info/smh#46ba073e-7d8f-4f90-84a7-1c4173349b9d"||	"EMCRM01DN -report"                            |
  |"http://smartlifehealth.info/smh#c7ddf211-e311-41c3-817c-b578dd4f0883"|                                 |	"EMCRM01AN -report"                     |
  |"http://smartlifehealth.info/smh#9bf26d52-f503-4637-9e04-40f33dcf0851"|                                               |	"EMCRM01EN -report"     |
  |"http://smartlifehealth.info/smh#abb29edf-dc94-4263-9dc7-7141c4c6dcd2"|                                               |	"*CRM02D  DENOMINATOR  Patients on CRM Register"|
  |"http://smartlifehealth.info/smh#98454693-208e-4dd1-83b2-4af3571b8abd"||	"CRM02a  All CRM  LAST 15M  HbA1c"|
  |"http://smartlifehealth.info/smh#5843685e-9167-439a-908d-b59d5360257b"|               |	"CRM02b  All CRM  LAST 15M  Blood Pressure"|
  |"http://smartlifehealth.info/smh#6c41d23d-e48d-44dc-95d3-25e98cf3168d"|                             |	"CRM02c  All CRM  LAST 15M  Lipids"|
  |"http://smartlifehealth.info/smh#b423251a-dbd1-49d1-ac80-ce365890abea"|             |	"CRM02d  All CRM  LAST 15M  Urine ACR"|
  |"http://smartlifehealth.info/smh#271a6ff8-11d7-4f94-a832-8ec274456df6"||	"CRM02e  All CRM  LAST 15M  eGFR"|
  |"http://smartlifehealth.info/smh#cbbfd426-4f27-47b0-a0c9-6b218b66a5e4"|                     |	"CRM02f  All CRM  LAST 15M  BMI"|
  |"http://smartlifehealth.info/smh#a5cae412-62e9-4973-9d2b-d1532d3945ad"|            |	"CRM02g  All CRM  LAST 15M  Waist circumference"                                                                  |
  |"http://smartlifehealth.info/smh#475c1acc-5db0-4c62-9e1f-6d5c7316034e"||	"CRM02h  All CRM  LAST 15M  Smoking Status"                                       |
  |"http://smartlifehealth.info/smh#e22b5616-6743-429f-8525-7fe2413f8b3f"| |	"Diabetic Patients"|
  |"http://smartlifehealth.info/smh#f165012f-4ecf-4742-8ff0-0b62f3ce1e0d"||	"EMCRM02 -report"                                                                     |
  |"http://smartlifehealth.info/smh#706656fc-14c3-460b-9eda-ded82505887a"|    |	"Metabolic dysfunction-associated steototic disease patients" |
  |"http://smartlifehealth.info/smh#f4fe1a28-33f1-43d7-8960-3becc276d32f"|                                                    |	"CRM02i  Diabetes & Mental Health Screening in last 15m OR No Diabetes"                                               |
  |"http://smartlifehealth.info/smh#4afa83be-a834-456b-a36e-9a7a547f28d1"|                                          |	"CRM02j  Diabetes & Right & Left Feet Risk Checks in last 15m OR No Diabetes"|
  |"http://smartlifehealth.info/smh#a4829e9d-ce3c-43e8-983f-a0d2e566d6b4"|                                                |	"CRM02k  Diabetes and Retinal Screening in last 27m OR No Diabetes"|
  |"http://smartlifehealth.info/smh#a57cd3ce-7b3e-4f20-8b6d-b7a232e386bf"||	"CRM02l  Diabetes or MASLD & FIB-4 in last 39m OR NO Diabetes or MASLD"|
  |"http://smartlifehealth.info/smh#1e639d61-787c-4ef4-bc3a-47dcb45dd760"|                                   |	"NO Diabetes or Metabolic dysfunction-associated steototic disease"                                                          |
  |"http://smartlifehealth.info/smh#f2c403c9-b143-44eb-ab3f-162d98b81a6a"|                                              |	"NHS NUMBERS  CRM02  Key Care Processes (more detailed) -report"                                              |
  |"http://smartlifehealth.info/smh#597e9300-1542-42a4-b17d-c0b9eb7ccbbd"|                                                 |	"NHS NUMBERS  CRM02  Key Care Processes (more detailed) -report"                                         |
  |"http://smartlifehealth.info/smh#e1ac4bed-ac68-428b-b17d-19293c63bcc6"|                                                             |	"CRM02  ACHIEVEMENT  Care Process Completed"                                                               |
  |"http://smartlifehealth.info/smh#0b654dd4-a6e7-424f-b5a9-f042d7bc3352"|              |	"NHS NUMBERS  CRM02  Key Care Processes (more detailed) -report"                                      |
  |"http://smartlifehealth.info/smh#7a1cb191-201a-4d7e-b661-8b2b9a87bda1"|                                                   |	"*CRM03D  DENOMINATOR  CKD, Diabetes or Hypertension"                                                                    |
  |"http://smartlifehealth.info/smh#7c339e21-7d3b-43ce-98bd-16950404aeed"|                                                            |	"EMCRM03 -report"|
  |"http://smartlifehealth.info/smh#2bf645e4-24f1-418f-a372-f5e16bdf51e4"||	"Patients with Moderate or Severe Frailty or aged >= 80"|
  |"http://smartlifehealth.info/smh#c6013995-75d3-4ca9-a4f0-37233d91f29b"|                                                        |	"Patients with no Moderate or Severe Frailty or aged < 80"     |
  |"http://smartlifehealth.info/smh#50f21b9e-73d3-432f-8407-eb8c2d1a3894"||	"CRM03A  NOT FRAIL or AGED < 80  LAST 15M  Latest BP <= 130/80"                                                     |
  |"http://smartlifehealth.info/smh#535c71af-e6ae-47b4-810e-7c6f4c2d9a55"|                                                                |	"CRM03B  FRAIL or AGED >= 80  LAST 15M  Latest BP <= 150/90"                                  |
  |"http://smartlifehealth.info/smh#34d545ba-ed7f-4573-9101-78452084b97d"|                                                     |	"CRM03  NHS NUMBERS  Blood Pressure Checklist -report"                     |
  |"http://smartlifehealth.info/smh#521b57de-bcfe-4fee-8719-2e35c83b8200"|                                                           |	"CRM03  NHS NUMBERS  Blood Pressure Checklist -report"                                         |
  |"http://smartlifehealth.info/smh#f1085ea8-45d6-4b6c-b355-ec4410cd103c"|                                                           |	"*CRM03  ACHIEVEMENT  LAST 15M  Latest BP <= appropriate target"                                           |
  |"http://smartlifehealth.info/smh#33d564e3-61d1-45af-8569-acf3577a6ee7"|             |	"CRM04D  DEN  Either CKD, CVD, DM, HF or AF, HYP, MASLD NDH & QRISK>10%"                          |
  |"http://smartlifehealth.info/smh#650e5b7c-dabf-4a2d-9c7f-aa9c33fc55cf"||	"CRM04  ACHIEVEMENT  LAST 6M  Moderate or High Intensity Statin"      |
  |"http://smartlifehealth.info/smh#199ee8b0-50a4-41d6-81d6-5cb096bee3f6"||	"EMCRM04 -report"           |
  |"http://smartlifehealth.info/smh#24e65d77-ec30-4225-9f70-cee25c179285"|                                                    |	"NHS NUMBERS  CRM04  Moderate or High Intensity statins -report"|
  |"http://smartlifehealth.info/smh#e21be2b8-94a2-4c63-9913-3aa9d5556d6e"|        |	"CRM05D  CKD & uACR >= 30 OR Diabetes & uACR >= 3 or eGFR < 60"|
  |"http://smartlifehealth.info/smh#5dfcb3cb-28e9-42a2-bd0b-fdb24262f4e0"|          |	"CRM05  ACHIEVE  LAST 6M  ACE inhibitor/Angiotensin Receptor Blocker"                                                  |
  |"http://smartlifehealth.info/smh#d9b283b9-224f-4149-a480-399457a1d492"||	"EMCRM05 -report"                               |
  |"http://smartlifehealth.info/smh#cd140ea7-4876-4acc-b905-b7c6bbdcaf68"|   |	"NHS NUMBERS  CRM05  ACE Inhibitor/Angiotensin Receptor Blocker -report"                                |
  |"http://smartlifehealth.info/smh#bae99a27-2143-448a-b891-878eb9077072"|                                         |	"CRM06D  CKD & eGFR btwn 20&45 OR CKD & uACR>=22.6 & eGFR btn 45&90 OR T2D OR HF"       |
  |"http://smartlifehealth.info/smh#da2968ca-3826-4118-8bbb-3ac982d0da1f"|                                |	"CRM06N  ACHIEVE  LAST 6M  SGLT-2 inhibitors"|
  |"http://smartlifehealth.info/smh#9fcd5ae9-3cfe-4d4c-9756-f46f6bf39d04"|        |	"EMCRM06 -report"|
  |"http://smartlifehealth.info/smh#c872a6d4-4bc5-4598-8a8f-8b0be88b00e4"|                   |	"NHS NUMBERS  CRM06  SGLT-2 inhibitors -report"|
  |"http://smartlifehealth.info/smh#f5034435-5289-4204-b934-02067e475666"|                                                                  |	"CRM07D  DEN  High or Moderate risk CRM Classification"                    |
  |"http://smartlifehealth.info/smh#f28ac66d-f3d3-4b35-99a8-391aa9aaaddf"|                                                          |	"CRM07a  LAST 15M  Care Plan" |
  |"http://smartlifehealth.info/smh#bc3fb2d4-3079-4dae-b7c5-a188c0ac547a"|                        |	"CRM07b  LAST 15M  Eat"                          |
  |"http://smartlifehealth.info/smh#343021e7-3d52-41c4-adfa-8a26c02ad2d1"|             |	"CRM07c  LAST 15M  Physical Activity"                                                          |
  |"http://smartlifehealth.info/smh#52be4276-5f28-468e-821b-39c84aab2f72"||	"CRM07d  LAST 15M  Sleep Pattern"|
  |"http://smartlifehealth.info/smh#0f083169-8549-470c-952e-153d682d5486"|                                                                 |	"CRM07e  LAST 15M  Relax"|
  |"http://smartlifehealth.info/smh#a38e6e20-5fe0-48a1-8888-46add1ab8dd8"||	"CRM07f  LAST 15M  Connect"     |
  |"http://smartlifehealth.info/smh#9156b306-ccab-4205-914b-638b3178efde"|                 |	"CRM07g  LAST 15M  Avoid harmful substances"                                        |
  |"http://smartlifehealth.info/smh#3eab2389-d8d5-48a1-8b7c-f3633d062897"||	"EMCRM07 -report"           |
  |"http://smartlifehealth.info/smh#88148629-8e19-474a-86b6-4ee466f3148d"||	"NHS NUMBERS  CRM07  Holistic Care Plan -report"|
  |"http://smartlifehealth.info/smh#3b8c46a0-5920-4c4c-82de-733929227cff"|                                                              |	"CRM07  ACHIEVEMENT  LAST 15M  Holistic Care Plan completed"|
  |"http://smartlifehealth.info/smh#c364e6c3-1301-4cd6-97a4-2e64ae2fcb5d"|                                                                    |	"CRM08AD  High or Moderate CRM  DEN  Earliest Inactive or moderate inactive"|
  |"http://smartlifehealth.info/smh#dc40bcc3-4084-4963-9648-53c0ca7469bd"|                                                                      |	"CRM08BD  High/Moderate CRM  DEN  Earliest BMI"|
  |"http://smartlifehealth.info/smh#bc265a80-05f5-4776-b69a-f139b35fd895"|             |	"CRM08CD  High or Moderate CRM  DEN  Earliest Current Smoker"                                                         |
  |"http://smartlifehealth.info/smh#84d2b02b-154d-40e3-9893-4b67a9a3d2b5"||	"CRM08A  ACHIEVEMENT  Latest Active codes recorded after inactive codes"|
  |"http://smartlifehealth.info/smh#a0c2d5cb-9a60-42b6-9bc6-2b65415b2ce0"||	"EMCRM08A -report"            |
  |"http://smartlifehealth.info/smh#10422256-044d-42d4-a219-84e698243e84"|                                                 |	"NHS NUMBERS  CRM08A  Exercise -report"|
  |"http://smartlifehealth.info/smh#98aadd7e-9c4c-4d87-95a8-781198737c97"||	"CRM08B  ACHIEVEMENT  Latest BMI recorded after earliest one"|
  |"http://smartlifehealth.info/smh#35adfe32-785a-412f-86bf-68b71c6bfecc"|                                   |	"EMCRM08B -report"|
  |"http://smartlifehealth.info/smh#6ead54e1-f184-4c43-8570-f63573e878a4"||	"NHS NUMBERS  CRM08B  BMI -report"|
  |"http://smartlifehealth.info/smh#44c675ce-a643-49d7-b29b-341cbdb94ff1"||	"CRM08C  ACHIEVEMENT  Latest Non-Smoker or Ex-Smoker"|
  |"http://smartlifehealth.info/smh#ccefd633-d2a6-4524-8711-c60440022212"|                                              |	"EMCRM08C -report"                                                     |
  |"http://smartlifehealth.info/smh#40b59aeb-f59e-4934-9bed-6d4fad602c35"||	"NHS NUMBERS  CRM08C  Smoking -report"|
  |"http://smartlifehealth.info/smh#ef7aa630-b120-45e8-b463-4eb6fa6e70e7"||	"CRM09D  DEN  High or Moderate Risk"           |
  |"http://smartlifehealth.info/smh#b50b85c3-a675-4ed2-9578-708cde7be541"|    |	"CRM09  ACHIEVEMENT  2 Health Confidence Score recorded at least 1 month apart"                                                      |
  |"http://smartlifehealth.info/smh#8ea0afd9-f8ee-439a-92e5-c0afb33abded"||	"EMCRM09 -report"                  |
  |"http://smartlifehealth.info/smh#d07b0ab6-3164-430f-a254-2eb5f11d8f71"|                                   |	"NHS NUMBERS  CRM09  2 Health Confidence Scores -report"|
  |"http://smartlifehealth.info/smh#24574306-a2c2-4e53-beca-6735e2367aca"||	"CHH01  Child Health hub conducted MISSING Enhanced Services Admin"        |
  |"http://smartlifehealth.info/smh#08eb9135-f9a7-4cce-aca3-e8fbe2b10e76"||	"CHH02  MISSING Patient Reported Experience Measure (PREM) offered"|
  |"http://smartlifehealth.info/smh#bf245d6b-2557-4955-9a41-ed416559773f"||	"CHH03  MISSING MDT Review"                             |
  |"http://smartlifehealth.info/smh#6233a8e5-1be7-40d7-96c8-b9cc53d70ab0"|      |	"ANONYMISED  DQ  POTENTIAL missing Enhanced Services Admin code -report"|
  |"http://smartlifehealth.info/smh#ec0d1c84-eaea-4554-bb0a-c3e4366f3927"|     |	"NHS NUMBERS  DQ  POTENTIAL missing Enhanced Services Admin code -report"|
  |"http://smartlifehealth.info/smh#a1d1a772-601d-4d91-bc24-f41838b30a51"|                                                           |	"ANONYMISED IDENTIFIER  DQ  MISSING PREM -report"                                                      |
  |"http://smartlifehealth.info/smh#62a66ef9-4bc5-4e8a-bd2f-5d9f8e3cbca2"|                        |	"NHS NUMBERS  DQ  MISSING PREM -report"            |
  |"http://smartlifehealth.info/smh#2a035b8e-78b2-41c4-962c-4b600c491e66"|          |	"ANONYMISED IDENTIFIER  DQ  MISSING MDT Review -report"                                                |
  |"http://smartlifehealth.info/smh#52bb950b-79ff-4b1f-8277-3b29334f85f5"||	"NHS NUMBERS  DQ  MISSING MDT Review -report"               |
  |"http://smartlifehealth.info/smh#cac3f134-a13c-43fc-ad83-d8616c1416d2"||	"CHH01  Child Health hub conducted"                   |
  |"http://smartlifehealth.info/smh#da59d478-9b1a-4ef5-9a09-212443e025e4"||	"CHH02  Patient Reported Experience Measure (PREM) offered"         |
  |"http://smartlifehealth.info/smh#3241a710-819b-44ad-a19f-2259df93218d"||	"CHH03  MDT Review"         |
  |"http://smartlifehealth.info/smh#28ca8580-e755-42da-9c79-73f30b545c2e"|                   |	"Activity Level Report"                     |
  |"http://smartlifehealth.info/smh#f1bddf21-f55d-43c5-aac7-c2955ef07511"|                                 |	"EMCHH01 -report"                                          |
  |"http://smartlifehealth.info/smh#ae866a88-fdaa-4e55-b7c2-565ca1f4208e"||	"Activity Level Report"                                                |
  |"http://smartlifehealth.info/smh#59af376f-787e-43f8-bc58-4118c943c8d1"||	"EMCHH02 -report"|
  |"http://smartlifehealth.info/smh#1e6c3c1a-226f-44be-abd7-44f0fb002228"|                                                        |	"Activity Level Report"                                                               |
  |"http://smartlifehealth.info/smh#4c45dcca-4934-477c-ae81-7bc1f2614874"||	"EMCHH03 -report"                                                               |
  |"http://smartlifehealth.info/smh#209cae92-bd18-4857-b673-bd5b6c00b42c"||	"CHH01  Child Health hub conducted"                                              |
  |"http://smartlifehealth.info/smh#5739f4c7-b59e-4de1-9406-733bde659134"|                     |	"CHH02  Patient Reported Experience Measure (PREM) offered"|
  |"http://smartlifehealth.info/smh#4ce6b924-3212-48ad-849c-8577053981f7"||	"CHH03  MDT Review"                                     |
  |"http://smartlifehealth.info/smh#434f6de5-a839-45d3-b91d-b4531603d516"|                     |	"Activity Level Report"                                                          |
  |"http://smartlifehealth.info/smh#0c0577a1-e48d-423d-bc22-59e88c31243b"||	"EMCHH01 -report"        |
  |"http://smartlifehealth.info/smh#2e079214-1b3d-43db-9d89-ffc52812e2e8"||	"Activity Level Report"                                             |
  |"http://smartlifehealth.info/smh#009a51d4-cc84-40b0-a6d8-9745033329f8"|                   |	"EMCHH02 -report"                                     |
  |"http://smartlifehealth.info/smh#0511c24b-5a74-445e-9980-8211714ffbf7"|                       |	"Activity Level Report"                                                                      |
  |"http://smartlifehealth.info/smh#29fd08a6-ba4b-433c-b4b6-3780107548d7"||	"EMCHH03 -report"                                                |
  |"http://smartlifehealth.info/smh#4a1a85d6-8268-41b8-81b2-e8bd1362f9d4"||	"CHH01  Child Health hub conducted"                                               |
  |"http://smartlifehealth.info/smh#e6e6dd7b-9d9d-4b3b-9032-db5004842865"||	"CHH02  Patient Reported Experience Measure (PREM) offered"      |
  |"http://smartlifehealth.info/smh#2148dc53-7dd0-490c-b105-6a20d20ca607"||	"CHH03  MDT Review"                                  |
  |"http://smartlifehealth.info/smh#947f8d05-c9f2-4f4e-9d38-593af056756b"|                                  |	"Activity Level Report"   |
  |"http://smartlifehealth.info/smh#157ec104-f640-40fd-8e19-14dab2f75095"|                |	"EMCHH01 -report"|
  |"http://smartlifehealth.info/smh#529b8268-8500-4e65-8122-1117a22b7205"|                                            |	"Activity Level Report"               |
  |"http://smartlifehealth.info/smh#061fe45c-bbf4-4915-968a-0337e19f7258"|                                                 |	"EMCHH02 -report"                                                   |
  |"http://smartlifehealth.info/smh#5a66ac84-3148-4261-9a8d-5a60c35b840d"|                                                        |	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#0a6372d7-4d89-4271-91d0-ad574751b2b9"|                              |	"EMCHH03 -report"|
  |"http://smartlifehealth.info/smh#de6e3372-464b-4d24-9522-796673e6ec5f"|                                     |	"CHH01  Child Health hub conducted"|
  |"http://smartlifehealth.info/smh#156970af-cce6-4694-bd70-d700147dd58f"|                                  |	"CHH02  Patient Reported Experience Measure (PREM) offered"                                       |
  |"http://smartlifehealth.info/smh#e3f42a15-0fc5-4e32-a05b-5978a627b828"|           |	"CHH03  MDT Review"                              |
  |"http://smartlifehealth.info/smh#5f6996ac-3aa9-434f-9610-f09276815a2b"|                          |	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#83a9c3a9-fb9c-4250-8b81-6f23d2a26a0b"|                                             |	"EMCHH01 -report"|
  |"http://smartlifehealth.info/smh#edbca959-5028-4498-979d-1a826abf5c69"|                                                    |	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#9b7e8c7c-5051-4503-ac2b-36983eafb118"|       |	"EMCHH02 -report"        |
  |"http://smartlifehealth.info/smh#adc5abfc-d535-42e2-8d75-272ff2ba4a26"|                          |	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#090fecf7-e0f9-4395-900b-43f7666debdd"|            |	"EMCHH03 -report"                |
  |"http://smartlifehealth.info/smh#48ad007e-233f-4146-bb9e-f48983628b71"|     |	"COF01-ES-Insertion or replacement of LNG-IUD WITHOUT Enhanced Services Admin"|
  |"http://smartlifehealth.info/smh#bd890b5e-4730-4328-9cb1-fa1c992cb578"|                                             |	"COF02-ES-Removal of LNG-IUD WITHOUT Enhanced Services Admin"|
  |"http://smartlifehealth.info/smh#3bcd0650-e9e6-4ff7-a525-94b297924153"|                                                              |	"Anonymised - DQ Report - POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#28a0568d-b02f-43a4-9f71-b3d5ae2c0dcd"||	"NHS Numbers - DQ Report - POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#bbc1d0f5-caf2-426c-84ee-a2e549f2fda2"|                                    |	"Anonymised - DQ Report - POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#f2282598-181d-47d3-89f4-9a61ec7fb630"|                                                          |	"NHS Numbers - DQ Report - POTENTIAL missing Enhanced Services Admin code"              |
  |"http://smartlifehealth.info/smh#ae5bea9d-d5cd-4e95-89e2-23a59bb29556"|                                                 |	"COF01-ES-PAYMENT-Insertion or replacement of LNG-IUD"|
  |"http://smartlifehealth.info/smh#f1c192b9-2b08-4010-8d2b-2d2467897ad1"|                                                                 |	"COF02-ES-PAYMENT-Removal of LNG-IUD"                                                                  |
  |"http://smartlifehealth.info/smh#ef282043-39e1-4416-9acd-d618d74566a1"|      |	"Activity Level Report"                |
  |"http://smartlifehealth.info/smh#320a2fbf-b335-4934-a257-42bbee888499"|            |	"EMCOF01 -report"                                       |
  |"http://smartlifehealth.info/smh#03ef0bbd-2b07-42e6-8605-cf8fc4d27f0e"|     |	"Activity Level Report"                                         |
  |"http://smartlifehealth.info/smh#1ce9caf7-3cc2-48ce-9bcc-5452d4c7ba74"|           |	"EMCOF02 -report"  |
  |"http://smartlifehealth.info/smh#2e778b27-48de-4bad-8e92-5af9a9f90645"||	"COF01-ES-PAYMENT-Insertion or replacement of LNG-IUD"|
  |"http://smartlifehealth.info/smh#545f402e-c475-483a-a630-9aa9ee4511c1"|                                                                  |	"COF02-ES-PAYMENT-Removal of LNG-IUD"                     |
  |"http://smartlifehealth.info/smh#79f87839-d43e-402e-b3de-3824b079f3eb"||	"Activity Level Report"               |
  |"http://smartlifehealth.info/smh#b866fab9-8924-44bd-9864-390da96a44cb"|             |	"EMCOF01 -report"|
  |"http://smartlifehealth.info/smh#8d52c0c5-c83e-409a-84a0-9f1018150064"|                  |	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#f6498bbe-b1e6-423c-82e1-d3bb1a3a928c"||	"EMCOF02 -report"|
  |"http://smartlifehealth.info/smh#d5088d9e-2b4a-407c-b462-49764a5fffb3"|                |	"COF01-ES-PAYMENT-Insertion or replacement of LNG-IUD"|
  |"http://smartlifehealth.info/smh#c9d066b3-ccc5-43cc-8e10-cce3ec429bdb"|                                                                     |	"COF02-ES-PAYMENT-Removal of LNG-IUD"                                 |
  |"http://smartlifehealth.info/smh#ba58223f-9090-4a44-a310-a13140fc75c2"||	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#50241572-ebbd-4398-a1ff-f32a5e04d885"|                |	"EMCOF01 -report"                               |
  |"http://smartlifehealth.info/smh#85473845-c28a-4903-a4dc-8afa0ef96548"|                                                        |	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#337c131a-a53e-4fb0-a79d-c0137b2caffd"||	"EMCOF02 -report"|
  |"http://smartlifehealth.info/smh#2d1c4044-ce8b-47c6-a64c-eb10a5a12368"||	"COF01-ES-PAYMENT-Insertion or replacement of LNG-IUD"|
  |"http://smartlifehealth.info/smh#813503cf-ce16-4b53-b28f-b99998d57b3a"|   |	"COF02-ES-PAYMENT-Removal of LNG-IUD"|
  |"http://smartlifehealth.info/smh#5578533f-d949-436a-adfd-5aa31ccd3025"||	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#f2acd0ee-ed27-4061-899e-5415c60cdb6b"|                                               |	"EMCOF01 -report"|
  |"http://smartlifehealth.info/smh#b45cfc23-8dfa-45e2-a310-ac9d40cda380"|                                                         |	"Activity Level Report"                                                 |
  |"http://smartlifehealth.info/smh#e17d8348-529c-4b85-a515-ca1fde47a3f4"|                                      |	"EMCOF02 -report"|
  |"http://smartlifehealth.info/smh#a969508c-f6eb-4526-8806-59bdfe3e7f7c"|                                                           |	"COF00 - Non-contraceptive indication"                                          |
  |"http://smartlifehealth.info/smh#ae45f8d7-14e5-44c4-9734-3ae3c8fc29ea"|                             |	"DL200-Patients who could be seen under MDT Review"       |
  |"http://smartlifehealth.info/smh#50647d08-076b-417a-829a-11df04556590"|                     |	"Patient List -report"                                 |
  |"http://smartlifehealth.info/smh#d10cbe38-4234-4fd7-a62f-eeff6be42692"|                                                   |	"DL201-ES-Patients discussed at MDT (in this Financial Year)"                                                    |
  |"http://smartlifehealth.info/smh#417deb24-e2cb-408c-9407-2822068badf9"|        |	"DL203-ES-Patients Insulin Initiated (in this Financial Year)"                              |
  |"http://smartlifehealth.info/smh#24f01860-543e-4095-85a3-c702600f239d"|        |	"DL204-ES-Patients GLP-1 Initiated (in this Financial Year)"              |
  |"http://smartlifehealth.info/smh#c0d230c0-4dd9-4fd8-ab62-f5615e1259dc"|  |	"DL205-ES-Patients with Insulin Optimisation/Intensification (in Financial Year)"                                             |
  |"http://smartlifehealth.info/smh#c515bbec-a3db-42e1-ac40-37376be81724"||	"Activity Level Report"                                       |
  |"http://smartlifehealth.info/smh#7a43a9cc-43bd-4382-84cd-266b8ebdcd75"|                             |	"EMDL201 -report"                                                        |
  |"http://smartlifehealth.info/smh#d8ce470c-fb8e-43ab-85b9-9e758384fcc2"|                                                            |	"Activity Level Report"                                                             |
  |"http://smartlifehealth.info/smh#85feb485-56b6-430c-82fb-345bdbac4ddb"|                                                    |	"EMDL203 -report"                                             |
  |"http://smartlifehealth.info/smh#106c3cad-c6fc-41f6-a77e-8fff34183109"|                                          |	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#e68db22d-8c80-483e-8856-5cccfde41d0e"|                                               |	"EMDL204 -report"                                              |
  |"http://smartlifehealth.info/smh#9c886662-a8fb-4e39-b64b-b9082640d1a4"|                                                      |	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#be3ab02c-2dce-4937-accc-daf2bfe56646"|                                               |	"EMDL205 -report"                     |
  |"http://smartlifehealth.info/smh#47e06897-2098-4846-9ee6-778fc4ef7c0e"|                                                      |	"CKD005 - Patients on the CKD register"|
  |"http://smartlifehealth.info/smh#9bc5481c-a8e5-4937-8068-06da00b9fb31"|                             |	"DM017 - Patients on Diabetes QOF Register"|
  |"http://smartlifehealth.info/smh#278afc63-caf6-4508-ba46-8daab73bc8c6"|                          |	"DM017 - Patients on Diabetes QOF Register (including deceased and deducted)"|
  |"http://smartlifehealth.info/smh#7f84b989-f461-4be4-8244-84c02740b184"|  |	"LD004 - Patients on the learning disabilities register"|
  |"http://smartlifehealth.info/smh#9ad06816-0477-4eb5-b9bc-4da5a5259e33"|                        |	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis"|
  |"http://smartlifehealth.info/smh#a44678cf-df38-45c1-b4d1-98fffe478a41"|                  |	"MH2_REG - Lithium treatment with prescription in last 6 months"   |
  |"http://smartlifehealth.info/smh#2fe1a2a9-edeb-4910-a529-fc3f0d939ade"|            |	"DL200b- Homeless Register - Patients who are homeless"|
  |"http://smartlifehealth.info/smh#435bc234-ffd8-422b-95ec-60bd600fc2ca"|                    |	"DL200c- Needle Phobia-Patients with Needle Phobia"|
  |"http://smartlifehealth.info/smh#2489ef88-b1ce-4f11-953c-ad1ccedffe24"|                         |	"DL200d-Patients on at least 3 Oral Diabetic Medication and latest HbA1c > 58"|
  |"http://smartlifehealth.info/smh#e29dcaa4-49f0-496e-a132-2e0f91c1d4dd"||	"DL200e-Patients with CVD"            |
  |"http://smartlifehealth.info/smh#278365b7-f68c-4adb-869a-2b6b258e0e39"|                                    |	"DL200f-Patients on CKD Register"      |
  |"http://smartlifehealth.info/smh#94f6ec99-b9a6-4af3-9922-0efb9d417285"||	"DL200g-Patients aged > 65 and on Diabetic Medication and latest HbA1c<48"|
  |"http://smartlifehealth.info/smh#dd94fb37-6df8-4167-94a0-32bfc2870d14"|                                                                   |	"DL200h-Housebound Patients and latest HbA1c > 58"|
  |"http://smartlifehealth.info/smh#389d141a-93ae-4cd3-9831-27a912e1ca0a"|            |	"DL200i-Patients in a Care Home and latest HbA1c > 58"|
  |"http://smartlifehealth.info/smh#4ddb0365-0d39-4a83-8d09-c7172b7b51bd"|                                                   |	"DL200j-Patients on LD Register"|
  |"http://smartlifehealth.info/smh#244a662f-3f27-4603-a444-702c1c237bcc"|                  |	"MH001 - Patients on the mental health register"|
  |"http://smartlifehealth.info/smh#9794bd33-4384-4d44-bf9c-d5e50a98730f"||	"DL208fD-ES-Patients initated or optimised on Insulin"             |
  |"http://smartlifehealth.info/smh#6d14ce8c-613b-4b96-a858-0b35c06f4f51"|            |	"DL200a- SMI Register - Patients with SMI (MH00)"        |
  |"http://smartlifehealth.info/smh#f324da1f-07c6-465b-9bdf-5843c2890099"|                                                                    |	"DL200-REGISTER-Patients aged 18-39 with Type 2 Diabetes"|
  |"http://smartlifehealth.info/smh#ce0ddb50-eadc-4c60-b49c-998b9c02f39f"|                                                            |	"EMDL200a -report"|
  |"http://smartlifehealth.info/smh#d380460a-1466-4d05-81ac-6bddd02f2f89"|          |	"Patient List -report"                                                           |
  |"http://smartlifehealth.info/smh#d2a96641-38dd-471d-b3e4-d4ca3f3c30cb"||	"DL202-Early Onset Type 2 Diabetes Review recorded (in this Financial Year)"|
  |"http://smartlifehealth.info/smh#902a59ed-b4f7-47ff-a789-15527ce8a292"||	"Activity Level Report"                                                    |
  |"http://smartlifehealth.info/smh#19c48b04-2609-4ee1-90cc-71b6a851760b"|                                          |	"EMDL202 -report"                 |
  |"http://smartlifehealth.info/smh#447110b3-2fcf-47bb-8dfd-d9dc0c0537c8"||	"DL207f-NUMERATOR-Referred to Weight Management Programmes"                  |
  |"http://smartlifehealth.info/smh#cfc6989e-5239-4c45-a95a-42a560caf825"||	"DL207g-NUMERATOR-Referred to ARRS Team"                       |
  |"http://smartlifehealth.info/smh#3fd46fa9-4aec-436b-a657-8412d8db2d98"||	"DL207h+iD-DENOMINATOR-Females Patients with Type 2 Diabetes"                                                         |
  |"http://smartlifehealth.info/smh#b00258a7-3fe9-4b84-8941-9f127beecb54"|       |	"EMDL207f -report"|
  |"http://smartlifehealth.info/smh#eb8b3688-5c9f-4e57-9e5b-bb5e4d452cd2"|                               |	"EMDL207g -report"|
  |"http://smartlifehealth.info/smh#b6f0af4c-cbc4-468f-8c93-05c4d2d9b9a3"|                                                         |	"DL207hN-NUMERATOR-Preconception Advice recorded"|
  |"http://smartlifehealth.info/smh#1e533947-2032-4d36-a238-28be5d6cf2fc"| |	"DL207iN-NUMERATOR-Folic Acid Prescribed"|
  |"http://smartlifehealth.info/smh#dcc94c00-d2c3-46f7-8252-92a3adb31aa1"|               |	"EMDL207hD -report"                                   |
  |"http://smartlifehealth.info/smh#58ac818b-3ffb-4e2c-aef4-214c942d2195"|                     |	"EMDL207hN -report"         |
  |"http://smartlifehealth.info/smh#14fa9bbc-9383-4c10-aa1b-abce4503af42"|                                |	"EMDL207iN -report"                                           |
  |"http://smartlifehealth.info/smh#65cb58fa-44dd-4d08-be1d-c3f31b903797"|                                      |	"DL202-REGISTER-Patients aged 18-39 with Type 2 Diabetes (deceased & deducted)"|
  |"http://smartlifehealth.info/smh#eaf04a9d-06ae-4e5e-b8d3-6bd184d63ff3"||	"E01a-ES-ECG Tests WITHOUT Enhanced Services Admin Code"|
  |"http://smartlifehealth.info/smh#592aee92-2ada-48ce-8923-5be1c5e4dfc8"||	"E01b-ES-ECG Interpretation WITHOUT ECG"                     |
  |"http://smartlifehealth.info/smh#a7ee8048-41b3-4f9a-ac24-29c30666c820"||	"E02-ES-Housebound Patients with ECG Tests WITHOUT Home Visit"|
  |"http://smartlifehealth.info/smh#3d8788eb-cbd9-4d3d-bde9-c40cb0c79f83"||	"Anonymised - DQ Report - POTENTIAL missing Enhanced Services Admin code"                                                               |
  |"http://smartlifehealth.info/smh#cd183e80-0fe6-43c2-8d8b-cf332e68483d"||	"NHS Numbers - DQ Report - POTENTIAL missing Enhanced Services Admin code"                                           |
  |"http://smartlifehealth.info/smh#49102064-3951-4d7d-933c-d98826b3f1d9"||	"Anonymised - Data Quality Report - finds missing ECG"                    |
  |"http://smartlifehealth.info/smh#282fa062-3b87-456b-b836-38a560d5ce78"||	"NHS Numbers - Data Quality Report - finds missing ECG"|
  |"http://smartlifehealth.info/smh#92295d55-380f-4ddc-9f76-b14c1c950235"||	"Anonymised - Data Quality Report - POTENTIAL missing Home Visit Code"                          |
  |"http://smartlifehealth.info/smh#40cee011-664b-4c60-b7ba-b97e6aa11a24"||	"NHS Numbers - Data Quality Report - POTENTIAL missing Home Visit Code"|
  |"http://smartlifehealth.info/smh#95f22e96-3784-4940-ab3c-d90cbcfcd6c5"||	"E01c-ES-ECG WITHOUT ECG Interpretation Codes"                         |
  |"http://smartlifehealth.info/smh#324a7594-2413-44b5-8655-c05d0f65a15b"||	"Anonymised - Data Quality Report - finds missing ECG Interpretation"|
  |"http://smartlifehealth.info/smh#8e72e6dd-241d-4077-ac5f-a79c06563f14"||	"NHS Numbers - Data Quality Report - finds missing ECG Interpretation"|
  |"http://smartlifehealth.info/smh#2062c0b7-171f-4928-9d5d-4e072fecb051"||	"E01a-ES-Consultations for ECG"    |
  |"http://smartlifehealth.info/smh#98454e6b-97c6-4ed0-a94b-d56343215769"||	"E01-ES-PAYMENT-ECG Interpretation recorded"                          |
  |"http://smartlifehealth.info/smh#a25fab67-8931-40cc-9b46-3fcc7aff3099"||	"E02-ES-PAYMENT-Home Visits for ECG"    |
  |"http://smartlifehealth.info/smh#bbac4210-9e0d-40ba-9182-58e64f4c1f57"||	"Activity Level Report"                                              |
  |"http://smartlifehealth.info/smh#2cdfcc7f-d617-4da3-aeef-df432377aff2"|    |	"Activity Level Report for Payment"                               |
  |"http://smartlifehealth.info/smh#c66fab51-8b5c-448b-9623-1d3462077c39"||	"EME01 -report"                                 |
  |"http://smartlifehealth.info/smh#ad0ecd0b-76f8-403e-86e4-a1639e85462c"||	"Activity Level Report for Payment"                            |
  |"http://smartlifehealth.info/smh#1b9acdaa-bade-4042-bad9-3e8d118885b2"|          |	"EME02 -report"                                         |
  |"http://smartlifehealth.info/smh#26a9ef9c-5d63-4659-bfa3-6fe72c5c8163"|                                       |	"E01a-ES-Consultations for ECG"|
  |"http://smartlifehealth.info/smh#367da9f6-7e2f-4922-ab30-bde46a54835a"||	"E01-ES-PAYMENT-ECG Interpretation recorded"|
  |"http://smartlifehealth.info/smh#44a12c59-ea4d-434d-a6be-a6da7c2bff93"||	"E02-ES-PAYMENT-Home Visits for ECG"                             |
  |"http://smartlifehealth.info/smh#9982801a-a9f5-4560-8466-bbf0da2f6906"||	"Activity Level Report"                                            |
  |"http://smartlifehealth.info/smh#b14fad94-4663-4ab3-aefe-5d4964cfe51f"|                               |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#618de104-fe05-4733-b70c-5f56593e12c2"||	"EME01 -report"                                        |
  |"http://smartlifehealth.info/smh#32faf624-28ab-4bc7-b7a8-9931dd703c06"|                                           |	"Activity Level Report for Payment"     |
  |"http://smartlifehealth.info/smh#50b65a5d-dbd6-4687-9389-89003bc45467"||	"EME02 -report"                                                        |
  |"http://smartlifehealth.info/smh#e8fae81f-a27b-48e0-8e7c-e732cebbbcb9"|                                   |	"E01a-ES-Consultations for ECG"     |
  |"http://smartlifehealth.info/smh#abeed600-bcc4-4e46-9ff8-d4c8b2ccb6b3"||	"E01-ES-PAYMENT-ECG Interpretation recorded"                       |
  |"http://smartlifehealth.info/smh#bbc512cd-a1a0-40c2-8da2-eb31af5375a6"|           |	"E02-ES-PAYMENT-Home Visits for ECG"                        |
  |"http://smartlifehealth.info/smh#73d17d7d-74ee-491f-a101-772e83f785c4"||	"Activity Level Report"|
  |"http://smartlifehealth.info/smh#5fd583ef-bc9c-4ad8-990d-ca020959769b"|             |	"Activity Level Report for Payment"                                          |
  |"http://smartlifehealth.info/smh#d159a783-de64-40b7-afe5-4a4428d2bba6"||	"EME01 -report"                                               |
  |"http://smartlifehealth.info/smh#22f8adf0-5d43-41ca-b3cf-a601da53db8d"|                               |	"Activity Level Report for Payment"                                                       |
  |"http://smartlifehealth.info/smh#86f5aadc-e5d4-4857-9802-4036abe6acb1"|                       |	"EME02 -report"                        |
  |"http://smartlifehealth.info/smh#89581250-5bf2-40ec-ab58-196b9ac6318b"|                                                         |	"E01a-ES-Consultations for ECG"      |
  |"http://smartlifehealth.info/smh#f7bcb6d5-5037-49b3-8c2e-633739f621fc"|                                 |	"E01-ES-PAYMENT-ECG Interpretation recorded"|
  |"http://smartlifehealth.info/smh#4ce37b63-f937-4bfe-99d7-e55beffe92d6"|                                                                |	"E02-ES-PAYMENT-Home Visits for ECG"                                     |
  |"http://smartlifehealth.info/smh#9978a4ce-9ea9-46ab-a984-ba01f0eed8d2"|                               |	"Activity Level Report"                         |
  |"http://smartlifehealth.info/smh#21adf6d3-750c-4d96-af8f-476dd2bc1737"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#7ad73445-47ad-409b-81b7-52075630b7c3"|                                                                    |	"EME01 -report"|
  |"http://smartlifehealth.info/smh#52dc9606-d0a2-47f3-8e09-09dee4f573ac"|  |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#698a0549-d33b-4e5f-b014-054ce2697a0a"||	"EME02 -report"                                                      |
  |"http://smartlifehealth.info/smh#86945970-df80-43b4-b913-beef3f645a6a"||	"E04D-ES-KPI-DEN-Patients with ECG conducted in Payment Period"|
  |"http://smartlifehealth.info/smh#cbd796b7-ea6f-4d03-bcf7-7dc92d56ed48"|                             |	"E04N-ES-KPI-NUM-Patients with ECG interpretation conducted within 3 days"                                                         |
  |"http://smartlifehealth.info/smh#ff623d84-8fa2-43fb-9564-f9410fcc12a7"|                                   |	"EME04D -report"                          |
  |"http://smartlifehealth.info/smh#eddb71f2-6da7-4550-a8b7-1b4ae7ab291e"| |	"EME04N -report"       |
  |"http://smartlifehealth.info/smh#706cadc4-9919-41bd-a811-532780d636f8"|                                               |	"TB00-ES-REGISTER-Patients eligible for Latent TB Screening Service"            |
  |"http://smartlifehealth.info/smh#b891f41f-9d47-484a-ab7c-94b5c4071404"||	"Register - Patient List -report"                                 |
  |"http://smartlifehealth.info/smh#2034627c-3eef-45d4-8d3b-c357347ed5f0"|                                                                    |	"TB03-ES-Patients with Positive IGRA Results MISSING Referral to TB Service"|
  |"http://smartlifehealth.info/smh#a0096334-1a55-438b-b371-1b1a1aa96a6e"||	"TB00a-Born or Lived in High Incidence Country MISSING Date of entry to UK"                                  |
  |"http://smartlifehealth.info/smh#ac2c4340-dbb1-4dd5-9e51-29f2123aee74"|                           |	"TB02a-ES-Patients invited for screening MISSING IGRA Results"                                       |
  |"http://smartlifehealth.info/smh#5512a342-b05f-45ae-a583-864c46af84b0"||	"TB01a-ES-Patients on LTBI Register MISSING Screening Invitation OR Declined"|
  |"http://smartlifehealth.info/smh#1f27a1fa-3121-4485-8751-1ab5b13788fc"||	"TB01b-ES-Patients invited for LTBI once or twice DUE to be invited"                                     |
  |"http://smartlifehealth.info/smh#967b19c2-2046-4a54-ad54-ee042b65d8b4"||	"Anonymised - Data Quality Report - finds missing Referral to TB Service"|
  |"http://smartlifehealth.info/smh#ddcf1396-cc34-4f33-bb30-2ad0fa8a3c55"||	"NHS Numbers - Data Quality Report - finds missing Referral to TB Service"                                  |
  |"http://smartlifehealth.info/smh#e24ccfac-8a7a-43a2-8db3-1821d55b640a"||	"Anonymised - Data Quality Report - finds missing Date of Entry to UK"|
  |"http://smartlifehealth.info/smh#88cf16cb-53f5-47f6-9cc0-d65d40d304cd"|                                                                 |	"NHS Numbers - Data Quality Report - finds missing Date of Entry to UK"                                                                     |
  |"http://smartlifehealth.info/smh#7bf6fb79-10f8-4207-b067-95e95050459f"||	"Anonymised - Data Quality Report - finds missing IGRA Results"                                     |
  |"http://smartlifehealth.info/smh#06c7e637-9783-41fd-ab34-d196df679d71"|    |	"NHS Numbers - Data Quality Report - finds missing IGRA Results"                            |
  |"http://smartlifehealth.info/smh#957327e2-2fec-4710-91c2-27495371ea40"|   |	"TB01c-ES-Patients invited 3 times for LTBI Screening MISSING Declined"|
  |"http://smartlifehealth.info/smh#c619592f-d4cc-4355-b120-cba9e3d574f0"||	"TB02b-ES-Patients with IGRA Results Recorded MISSING from Eligibility Cohort"|
  |"http://smartlifehealth.info/smh#3321fdf6-9800-4b35-98fb-9ad51771080d"||	"TB02c-ES-IGRA Results recorded MISSING Birth or Lived in High Risk Country"                                             |
  |"http://smartlifehealth.info/smh#383978ad-2c3e-464d-aa88-bb962eda92ad"||	"TB02d-ES-IGRA Results recorded MISSING Date of Entry to UK"                          |
  |"http://smartlifehealth.info/smh#9fc93bd0-9028-4840-99d8-92d70a265a42"||	"Anonymised - Data Quality Report - finds missing Screening Invitation"|
  |"http://smartlifehealth.info/smh#125272b3-9a92-4785-bc8d-504629d0107b"||	"NHS Numbers - Data Quality Report - finds missing Screening Invitation"|
  |"http://smartlifehealth.info/smh#c1ef62fa-27b4-4fc0-a47d-d6881123968e"||	"Anonymised - Data Quality Report - finds missing Screening Invitation"|
  |"http://smartlifehealth.info/smh#7f5bf2cb-614e-47ba-8179-8ce492b5e34b"||	"NHS Numbers - Data Quality Report - finds missing Screening Invitation"|
  |"http://smartlifehealth.info/smh#ea62690b-59bd-4f46-8806-a25006804c06"|                                                                |	"Anonymised - Data Quality Report - finds missing Screening Declined"                                      |
  |"http://smartlifehealth.info/smh#ad5f2554-9ffd-4f63-9b96-aca5aa008d1f"||	"NHS Numbers - Data Quality Report - finds missing Screening Declined"     |
  |"http://smartlifehealth.info/smh#27f38074-b0ef-4f5c-841c-1a9883faa987"||	"Anonymised - Data Quality Report - finds missing patients on eligbility cohort"|
  |"http://smartlifehealth.info/smh#6d0d7ec0-5e37-4491-bc0c-00cf13cb6a76"||	"NHS Numbers - Data Quality Report - finds missing patients on eligbility cohort"|
  |"http://smartlifehealth.info/smh#c3b3a0ab-311f-4e17-8e30-c6653060db86"|                                                            |	"Anonymised - DQ Report - finds missing Birth or Lived in High Risk Country"                                               |
  |"http://smartlifehealth.info/smh#1f7cb3b6-a890-4082-911c-44d5a761693f"||	"NHS Numbers - DQ Report - finds missing Birth or Lived in High Risk Country"|
  |"http://smartlifehealth.info/smh#7ea28fc0-af3e-45d1-a0e5-132a32d9998b"||	"Anonymised - DQ Report - finds missing Birth or Lived in High Risk Country"|
  |"http://smartlifehealth.info/smh#e73592c0-4ac9-4777-9e29-bd58ff8a4eb5"||	"NHS Numbers - DQ Report - finds missing Birth or Lived in High Risk Country"|
  |"http://smartlifehealth.info/smh#78ad4755-0d78-47bd-9ea2-3581c7db3571"||	"TB04D-ES-KPI-DEN-Patients eligible for Latent TB Screening NOT LIVE REGISTER"|
  |"http://smartlifehealth.info/smh#4c716f4c-944a-4eb8-be05-620423e641f7"||	"TB05D-ES-Patients with postive IGRA result (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ccd3cefc-a540-43bc-98e4-021fcc12be1c"||	"ETB04D -report"|
  |"http://smartlifehealth.info/smh#9bf91c47-de10-4884-bd77-9c3e5e2f8447"|          |	"ETB05D -report"                                 |
  |"http://smartlifehealth.info/smh#d20fd766-d8c2-4655-872f-8244f6494c05"|                           |	"TB05N-ES-KPI-NUM-Patients referred to TB service (in Financial Year)"             |
  |"http://smartlifehealth.info/smh#4c31b4df-f7cc-4c0f-ad53-f91207374a5c"||	"TB04N-ES-KPI-NUM-Patients had been tested (in Financial Year)"                                                              |
  |"http://smartlifehealth.info/smh#5a1576da-4b1f-4253-b8e4-990f1dd1f223"||	"ETB05N -report"|
  |"http://smartlifehealth.info/smh#fcd91a57-8afb-428d-bdb8-d6438d8793a1"|               |	"ETB04N -report"      |
  |"http://smartlifehealth.info/smh#daaef105-e77e-480e-a94a-6fe85d231b7b"|                                      |	"TB01-ES-Patients invited 3 times or have declined"|
  |"http://smartlifehealth.info/smh#56422852-35ed-4e7f-84ec-4c9d5f1246b5"|     |	"TB02-ES-Patients with IGRA Results recorded"|
  |"http://smartlifehealth.info/smh#9d98c3a3-9f2b-481e-adaf-95d421aa2a68"|                        |	"TB03-ES-Patients referred to TB service following postive IGRA result"             |
  |"http://smartlifehealth.info/smh#9fe8ed01-8b6d-4af7-91e4-ef84c2e264da"|                                                                      |	"Activity Report for Payment"|
  |"http://smartlifehealth.info/smh#5124ace6-64a1-4058-861e-acb4366a6032"|                 |	"ETB01 -report"     |
  |"http://smartlifehealth.info/smh#9d7d7212-86a2-4970-9ecb-d1decdf50504"|                                      |	"Activity Report for Payment"  |
  |"http://smartlifehealth.info/smh#f1cbefea-2b8c-4db7-854f-d5240687f4c6"|                                  |	"ETB02 -report"                                                              |
  |"http://smartlifehealth.info/smh#be882819-4ca1-4383-9526-1fc84051a113"|                                            |	"Activity Report for Payment"                                                 |
  |"http://smartlifehealth.info/smh#79a3e1d1-4138-4ebb-8eae-cf7ac2a3571c"|                         |	"ETB03 -report"                                 |
  |"http://smartlifehealth.info/smh#bfa4fe97-b554-404f-8059-7d6eabd04f8d"|                                          |	"LTBI00-Patients eligible for Latent TB Screening (inc deceased & deducted)"                                                  |
  |"http://smartlifehealth.info/smh#2690897e-f407-4159-a608-d38870118f76"||	"TB00-ES-Patients previously referred or invited for screening (exclusion)"|
  |"http://smartlifehealth.info/smh#1e08b6bf-8a42-4a02-9745-2d886fca35e0"|  |	"LTBI00 -report"                            |
  |"http://smartlifehealth.info/smh#917d9116-a35e-4ae5-a9b1-3918608b4653"|                                           |	"Depression Register (to calculate CCMI target)"                    |
  |"http://smartlifehealth.info/smh#d17d3926-282d-435e-b477-9ade6b60be91"|                          |	"SMI Register - Patients with SMI (MH001)"                          |
  |"http://smartlifehealth.info/smh#8169ed6d-ccec-450d-9eb9-f806786ccb7d"|                                      |	"Patients on Serious Mental Illness (SMI) Register (excluding those in remission)"                                      |
  |"http://smartlifehealth.info/smh#db8660d9-ff88-4343-a1d3-818aad8831c6"||	"CCMI - Patients who can be seen under MH service"       |
  |"http://smartlifehealth.info/smh#57521bd7-7336-4c93-990a-1317b25df040"||	"Patients on Complex Common Mental Health Illness (CCMI) Register (exc SMI)"                                    |
  |"http://smartlifehealth.info/smh#96bca0e6-37be-49e1-b533-0b61f43918bb"||	"SMI Register - MDS Report - Anonymised Identifier"                                                               |
  |"http://smartlifehealth.info/smh#ec1bda39-8f64-49d9-af05-24c882b22c32"|              |	"SMI Register - MDS Report - More Detailed - NHS Numbers"                                                      |
  |"http://smartlifehealth.info/smh#9758c593-b949-431e-a6d7-4eab2c9f9a75"||	"SMI Register - MDS Report - NHS Numbers"                   |
  |"http://smartlifehealth.info/smh#f1bbb6db-48de-459c-bb53-89bb0062e8e9"|                  |	"CCMI Register - MDS Report - Anonymised Identifier"|
  |"http://smartlifehealth.info/smh#22e1367b-57ef-4096-940b-a931b7c0e49a"|             |	"CCMI Register - MDS Report - More Detailed - NHS Numbers"                                                                |
  |"http://smartlifehealth.info/smh#106497a4-7a75-4a8c-9a37-2c9cac94e02f"|       |	"CCMI Register - MDS Report - NHS Numbers"     |
  |"http://smartlifehealth.info/smh#0497f8f7-7223-4f97-a44e-aeaf2197790c"|       |	"MH16D-DEN-Patients seen under the service (in last 12m)"                                                          |
  |"http://smartlifehealth.info/smh#ef4ba84a-3397-4ee5-9881-8cec28fdbd7a"||	"MH18D-DEN-Patients with Annual Review and Care Plan (in last 12m)"                               |
  |"http://smartlifehealth.info/smh#4ddb526f-73da-48ea-b069-0d9c2a00fbd2"||	"EMMH16D -report"|
  |"http://smartlifehealth.info/smh#30e74338-b504-4341-b0a5-1b01f8de2bad"|                       |	"MH16N-NUM-Patients with Annual Review and RaSWP recorded (in last 12m)"|
  |"http://smartlifehealth.info/smh#37182068-782c-435d-a4cd-3258312abad5"||	"MH19aD-DEN-Patients seen under the service and Smokers (in last 12m)"         |
  |"http://smartlifehealth.info/smh#2f29e33b-e443-4606-a510-c163cc4c43d3"||	"MH19bD-DEN-Patients on Anto-psychotics and QRISK=>20 (in last 12m)"|
  |"http://smartlifehealth.info/smh#9f347db9-ed81-4ace-9a8e-92b210f91396"||	"MH20aD-DEN-Patients with HbA1c between 42 and 47 (in last 12m)"                                                     |
  |"http://smartlifehealth.info/smh#8d4cdb2a-aa28-4529-8117-8997f1c99101"||	"MH20bD-DEN-Patients with HbA1c more than 48 (in last 12m)"                           |
  |"http://smartlifehealth.info/smh#34e3d435-f5e2-41ad-847d-d902eab504d4"|            |	"MH20cD-DEN-Patients with Poor Diet recorded (in last 12m)"                                                    |
  |"http://smartlifehealth.info/smh#7df07cbb-52b5-4ea3-9e12-73684703c453"|                  |	"MH20dD-DEN-Patients with No/Light Exercise recorded (in last 12m)"|
  |"http://smartlifehealth.info/smh#5b7170aa-991b-4d93-9e29-08c58f592a86"||	"MH20fD-DEN-Patients with Alcohol Intake > 14 units per wk(in last 12m)"|
  |"http://smartlifehealth.info/smh#7eea765f-e6ed-466a-a8a6-f74ca05d5bda"|  |	"MH20gD-DEN-Patients with Substance Misuse recorded (in last 12m)"|
  |"http://smartlifehealth.info/smh#dbfd051a-d42a-4b67-9454-9f7b64fd6d71"|               |	"MH21D-DEN-Patients seen under the service (in last 12m)"|
  |"http://smartlifehealth.info/smh#8a3f5f42-67d5-4c65-bb72-069b3a1f2dc5"|                                         |	"MH22D-DEN-Patients seen under the service (in last 12m)"|
  |"http://smartlifehealth.info/smh#3146405e-7576-428b-b890-d16b3b7c2a9d"||	"EMMH18D -report"|
  |"http://smartlifehealth.info/smh#5c1926c3-fad0-4fe4-985b-b21b7b1bf5a8"||	"MH18N-NUM-Patients provided a copy of care plan (in last 12m)"|
  |"http://smartlifehealth.info/smh#89fae32f-29f7-40b1-9ddb-4e87ae74b8f5"||	"EMMH16N -report"                                          |
  |"http://smartlifehealth.info/smh#47be5b74-f741-42c9-bd45-2b6457fc2adf"|          |	"EMMH19aD -report"                                     |
  |"http://smartlifehealth.info/smh#70ada349-511f-47fa-a328-34bc661c2f0e"||	"MH19aN-NUM-Patients with Cough and MRC Recorded (in last 12m)"|
  |"http://smartlifehealth.info/smh#0e5bc507-62b8-41fe-b2ba-1a59bb312d86"|                                        |	"MH20eD-DEN-Patients recorded as Smokers (in last 12m)"|
  |"http://smartlifehealth.info/smh#091c43fa-4e29-4c2c-83ba-2baecf826520"|                                                  |	"EMMH19bD -report"                                                                 |
  |"http://smartlifehealth.info/smh#dfa22af6-e9c3-437e-8e06-52834423edce"||	"MH19bN-NUM-Patients with ECG recorded (in last 12m)"    |
  |"http://smartlifehealth.info/smh#d04627c0-642d-411e-9f49-2c8d494ab06b"||	"EMMH20aD -report"                                                          |
  |"http://smartlifehealth.info/smh#ea42d49b-4b57-4a8f-a769-897813ddd8b2"||	"MH20aN-NUM-Patients referred to Pre-diabetes Prevention Programme(in last 12m)"|
  |"http://smartlifehealth.info/smh#0dfb0ff7-8f48-4cd3-97fe-08ec5ec8f6f3"||	"EMMH20bD -report"                                    |
  |"http://smartlifehealth.info/smh#f3655226-38a5-4be4-bc04-aee2370e58ab"|              |	"MH20bN-NUM-Patients referred to Diabetes Structured Programme (in last 12m)"|
  |"http://smartlifehealth.info/smh#369d2ce9-aff1-48c9-8907-3a548ade5b8f"||	"EMMH20cD -report"                                              |
  |"http://smartlifehealth.info/smh#324a6a8e-b5d1-4bcd-8a13-206322fb0a2b"|                                |	"MH20cN-NUM-Patients received Weight Management advice (in last 12m)"|
  |"http://smartlifehealth.info/smh#bae9c8d4-6506-4319-a862-ee84a0c40e58"|    |	"EMMH20dD -report"                  |
  |"http://smartlifehealth.info/smh#2a17f1c1-123f-41f8-ab4b-98fa94ab08a3"|                                      |	"MH20dN-NUM-Patients received Exercise/Lifestyle advice (in last 12m)"                                                              |
  |"http://smartlifehealth.info/smh#145a7b95-1c06-4706-bb7e-2f8dbfc06c26"||	"EMMH20fD -report"|
  |"http://smartlifehealth.info/smh#0b02dafc-f892-4540-bf9c-3552d265384a"|                      |	"MH20fN-DEN-Patients given Alcohol advice (in last 12m)"                                                           |
  |"http://smartlifehealth.info/smh#b072b5e6-81f8-4f17-8a73-926ded2fc25c"|       |	"EMMH20gD -report"                             |
  |"http://smartlifehealth.info/smh#8e71ac01-6bf3-4b99-84f4-014e0145f9ef"|                                                               |	"MH20gN-NUM-Patients given Substance Misuse advice (in last 12m)"           |
  |"http://smartlifehealth.info/smh#ba8de24b-55fa-41f5-b325-657c2972c6a0"|        |	"MH21N-NUM-Patients completed a year of care (in last 12m)"                                         |
  |"http://smartlifehealth.info/smh#82b28829-729c-478c-afdf-ae721f95ed30"|                |	"MH22aN-NUM-Patients with Flu Vaccinations Given/Declined/Contraindicted"                   |
  |"http://smartlifehealth.info/smh#b16f0dee-a9e6-4f09-af38-c9c348d7c058"||	"MH22bN-NUM-Patient with COVID vac Given/Declined/Contraindicted"                                             |
  |"http://smartlifehealth.info/smh#0a0be1a8-0160-4c0f-b37c-41645689e39b"|       |	"EMMH18N -report"                     |
  |"http://smartlifehealth.info/smh#f8401f85-bcdf-4856-8d7c-8201bfe1b233"|                                                       |	"EMMH19aN -report"|
  |"http://smartlifehealth.info/smh#35bc1d51-4e13-4a12-a8c5-8287786a4df9"|                                                       |	"MH20eN-NUM-Patients offered Smoking Cessation Advice (in last 12m)"|
  |"http://smartlifehealth.info/smh#3c7fdbae-c202-4e11-a129-b0db6dfb689f"||	"EMMH19bN -report"|
  |"http://smartlifehealth.info/smh#a577b727-0554-4e4b-b206-56583ac7eb5b"|    |	"EMMH20aN -report"               |
  |"http://smartlifehealth.info/smh#dc0a1cbb-2fa0-49d5-b850-290ceab9d8a4"||	"EMMH20bN -report"                                                       |
  |"http://smartlifehealth.info/smh#3a4a1c0a-1910-4b3c-a447-aeb003e79736"|                                    |	"EMMH20cN -report"  |
  |"http://smartlifehealth.info/smh#033e3497-b60e-4028-9e03-67b85b1ccfc4"|                |	"EMMH20dN -report"                             |
  |"http://smartlifehealth.info/smh#1199c061-40a1-4fed-9974-6ac3acf6944f"||	"EMMH20fN -report"                             |
  |"http://smartlifehealth.info/smh#2bc4bd30-3dcc-4624-aa77-1f749737c023"|                                    |	"EMMH20gN -report"         |
  |"http://smartlifehealth.info/smh#a37c94a0-fa1b-40c7-87b1-91968ec9cb54"|                |	"EMMH21N -report"                                                |
  |"http://smartlifehealth.info/smh#7c46119e-6dfb-466e-8c2c-bd76f3dbb84c"||	"EMMH22aN -report"                                                       |
  |"http://smartlifehealth.info/smh#a7004bcd-3585-482d-87d6-00af09afa899"|                                         |	"EMMH22bN -report"                |
  |"http://smartlifehealth.info/smh#64e72116-d6b6-489d-91b5-13bac7a5c720"|                                                            |	"EMMH20eN -report"|
  |"http://smartlifehealth.info/smh#ea0fc2ae-ab53-4b65-856d-1e2d139920e8"|                                                           |	"MH17D-DEN-Patients on SMI register (exc patients in remission)"|
  |"http://smartlifehealth.info/smh#ade3bc87-02fe-4578-ac14-e1ed0af4e915"||	"EMMH17D -report"|
  |"http://smartlifehealth.info/smh#96f79f11-5901-46d0-9421-75447fa87a2b"|                                        |	"MH17N-NUM-Patients seen under the service (in last 12 months)"|
  |"http://smartlifehealth.info/smh#d55fd09a-970a-4f22-8e27-4e91f71fa9d2"||	"EMMH17N -report"        |
  |"http://smartlifehealth.info/smh#6848cf2b-dbc4-40ac-91bc-b599b9bf6a8d"|                                         |	"Dashboard - Patients on SMI or CCMI Register"                                         |
  |"http://smartlifehealth.info/smh#d0b8d604-e4e7-4c0c-90db-ce91f7e6f9f8"|              |	"Mental Health Dashboard -report"         |
  |"http://smartlifehealth.info/smh#0c92813c-1dd9-4904-962b-0c29dae2bff6"|                          |	"MH00-SMI or CCMI Patients"|
  |"http://smartlifehealth.info/smh#ffd5fbbf-8905-45be-9c4e-54b3818dbc50"|                                |	"MH01-DQ-SMI or CCMI Patients WITHOUT BMI Completed"                                               |
  |"http://smartlifehealth.info/smh#360b35b7-0420-460c-bea6-8223a22d63f4"|      |	"MH02-DQ-SMI or CCMI Patients WITHOUT BP Completed"|
  |"http://smartlifehealth.info/smh#96ad411c-bd5b-4337-b83a-177712fde560"|             |	"MH03-DQ-SMI or CCMI Patients WITHOUT Diet Status Completed"                                                          |
  |"http://smartlifehealth.info/smh#27da21ce-14dc-400e-9a01-83e7a27e1ed9"|   |	"MH04-DQ-SMI or CCMI Patients WITHOUT Exercise Assessment Completed"       |
  |"http://smartlifehealth.info/smh#05a6edf8-bda0-4f5b-a9f4-666d046add25"|      |	"MH05-DQ-SMI or CCMI Patients WITHOUT Smoking Status Completed"        |
  |"http://smartlifehealth.info/smh#814c718f-8f31-4c51-8cb1-7b60bf22e9e8"|          |	"MH06-DQ-SMI or CCMI Patients WITHOUT Alcohol Intake Completed"|
  |"http://smartlifehealth.info/smh#88d44dec-5d6a-4a62-a415-a2bbdb29bfbb"||	"MH07-DQ-SMI or CCMI Patients WITHOUT Substance Abuse Completed"|
  |"http://smartlifehealth.info/smh#74a46aa8-f0c3-48b4-b20e-f61e0566e610"||	"MH08aD-DQ-SMI or CCMI Patients eligible for Cerivcal Cancer Screening"|
  |"http://smartlifehealth.info/smh#ea70d91e-ec23-43f8-af29-2fde8f76c588"||	"MH08bD-DQ-SMI or CCMI Patients eligible for Breast Cancer Screening"|
  |"http://smartlifehealth.info/smh#2db2ea1a-ddca-43a4-a35c-da306a8e8a99"||	"MH08cD-DQ-SMI or CCMI Patients eligible for Bowel Cancer Screening"|
  |"http://smartlifehealth.info/smh#23225b4a-6617-40e1-8c8f-688dbe5c306f"||	"MH09a-DQ-SMI or CCMI Patients WITHOUT RaSWP Completed"|
  |"http://smartlifehealth.info/smh#6a0197e2-5518-4284-80d0-80fcff44aa66"||	"MH09b-DQ-SMI or CCMI Patients WITHOUT Signs Unwell Completed"|
  |"http://smartlifehealth.info/smh#69533953-5473-4c59-abcf-fb6199a57cdd"||	"MH09c-DQ-SMI or CCMI Patients WITHOUT Anticipatory Care Plan Completed"|
  |"http://smartlifehealth.info/smh#06dc59a8-e83e-4e5b-8250-7d8ab89f8221"||	"MH09d-DQ-SMI or CCMI Patients WITHOUT Health Action Plan Completed"|
  |"http://smartlifehealth.info/smh#39652e46-d308-4d17-a838-6e690e8be824"||	"MH09e-DQ-SMI or CCMI Patients WITHOUT Patient Goals Completed"|
  |"http://smartlifehealth.info/smh#2882f35a-41e1-4b56-9b03-d4c8c7aaa65b"||	"MH10-DQ-SMI or CCMI Patients WITHOUT Medication Review Completed"|
  |"http://smartlifehealth.info/smh#47ec15fe-1956-4e35-bb09-4014357c93a8"|                                                     |	"MH11a-DQ-Anti-psychotics WITHOUT Serum Cholesterol in FY"                                               |
  |"http://smartlifehealth.info/smh#6c885ee8-59ae-4233-a4ee-9a487975e456"|                                                                   |	"MH11b-DQ-Not on Anti-psychotics WITHOUT Serum Cholesterol"|
  |"http://smartlifehealth.info/smh#2cbd5b42-95cf-4989-ac57-5c685b98fd0c"|                                                                  |	"MH11-DQ-SMI or CCMI Patients WITHOUT Serum Cholesterol completed" |
  |"http://smartlifehealth.info/smh#7cebb52a-d8e2-414d-8ead-b77d2b3a2506"||	"MH12a-DQ-Anti-psychotics WITHOUT HbA1c/Blood Glucose in FY"|
  |"http://smartlifehealth.info/smh#dc5b6e35-803c-4c58-b695-964ef6e1b72e"||	"MH12b-DQ-Not on Anti-psychotics WITHOUT HbA1c/Blood Glucose"|
  |"http://smartlifehealth.info/smh#fd17e470-b711-46d7-9f19-89f050fdf917"|                              |	"MH12-DQ-SMI or CCMI Patients WITHOUT HbA1c/Blood Glucose Completed"|
  |"http://smartlifehealth.info/smh#f3397264-8840-4e8b-b765-13a24ddc52a0"|                                                         |	"MH13D-DQ-SMI or CCMI Patients on Lithium"                                                         |
  |"http://smartlifehealth.info/smh#1e021a3b-b0ad-46af-84c8-22cfda380d54"|     |	"MH14-DQ-SMI or CCMI Patients WITHOUT First Appt Completed"      |
  |"http://smartlifehealth.info/smh#c48613b5-e3e1-4860-8389-6a73be9b8e80"||	"MH15-DQ-SMI or CCMI Patients WITHOUT Follow Up NOT on same day as 1st Appt"|
  |"http://smartlifehealth.info/smh#bb968982-b66f-43d3-a485-de1100043d1a"|                                   |	"MH08aN-DQ-SMI or CCMI Patients WITHOUT Advice for Cervical Cancer Screening"                |
  |"http://smartlifehealth.info/smh#32f5805a-1eb3-4053-99ee-16f55ee465b1"|                                                |	"MH08bN-DQ-SMI or CCMI Patients WITHOUT Advice for Breast Cancer Screening"     |
  |"http://smartlifehealth.info/smh#acd46739-3ce0-46c3-9afe-1701ee1852d7"||	"MH08cN-DQ-SMI or CCMI Patients WITHOUT Advice for Bowel Cancer Screening"                                                                   |
  |"http://smartlifehealth.info/smh#55ca7d04-9336-4c90-9ff6-bb7be27ece93"|                                                    |	"MH13Na-DQ-SMI or CCMI Patients WITHOUT Serum Lithium Completed"                                         |
  |"http://smartlifehealth.info/smh#9ae84d13-f90a-44b6-9c31-a706ba557691"|                                                             |	"MH13Nb-DQ-SMI or CCMI Patients WITHOUT eGFR Completed"            |
  |"http://smartlifehealth.info/smh#ab039076-c658-4a08-ad96-27b05e878512"||	"MH13Nc-DQ-SMI or CCMI Patients WITHOUT Serum TSH Completed"|
  |"http://smartlifehealth.info/smh#5d7cbe5a-1f5d-46e7-9c84-4508a4c4e9fe"|                                                                    |	"MH01-ES-Patients with BMI recorded (in FY)"              |
  |"http://smartlifehealth.info/smh#7eefbcab-f8f5-4020-9e2d-2945b51cf59e"|                                           |	"MH03-ES-Patients with Diet Status recorded (in FY)"                                 |
  |"http://smartlifehealth.info/smh#7ecaf15e-3b8e-4357-8e97-f2b63de23cbe"|                                                 |	"MH04-ES-Patients with Exercise Assessment recorded (in FY)"                   |
  |"http://smartlifehealth.info/smh#5ff50f7e-8cf1-4414-8ed2-0110cc868931"|                                                     |	"MH05-ES-Patients with Smoking Status recorded (in FY)"                                                              |
  |"http://smartlifehealth.info/smh#d34cbbf4-b5bc-48bf-a96b-cb452e1e9722"|                                                          |	"MH06-ES-Patients with Alcohol Intake recorded (in FY)"               |
  |"http://smartlifehealth.info/smh#29344d3d-7ce7-4589-b80c-a1056d041fe3"|                                                          |	"MH07-ES-Patients with Substance Misuse recorded (in FY)"             |
  |"http://smartlifehealth.info/smh#b289d3e5-d696-43fc-8486-9189bf4c16b5"|                                                        |	"MH08aa-MDS-Female patient aged 25-64 with no history of hysterectomy"                                            |
  |"http://smartlifehealth.info/smh#c34abe7e-e157-40e9-9899-f20be536db0f"|                        |	"MH08ba-ES-Female patient aged 50-70"                             |
  |"http://smartlifehealth.info/smh#e53a51b5-f4db-49c6-a3a4-87c1a97d2055"||	"MH08ca-ES-Patients aged 60-74"|
  |"http://smartlifehealth.info/smh#fc7e12f8-a073-413c-bd7e-63bec7ab48a2"|                                                                |	"MH09a-ES-Patients with RaSWP recorded (in FY)"|
  |"http://smartlifehealth.info/smh#3a97fbee-ba47-4b6f-a787-ba562f61ad43"|                                                                  |	"MH09b-ES-Patients with Signs Unwell recorded (in FY)"|
  |"http://smartlifehealth.info/smh#7dd84f15-d0ab-4e8d-af68-d38543fbd8c6"|                                                           |	"MH09c-ES-Patients with Anticipatory Care Plan recorded (in FY)"                                                                   |
  |"http://smartlifehealth.info/smh#6e21135e-1893-47e7-b3d4-6cbf5ae0eecd"|                                       |	"MH09d-ES-Patients with Health Action Plan recorded (in FY)"                 |
  |"http://smartlifehealth.info/smh#bda7c8cb-826e-48a3-adec-e6b752dd88b8"|                                                 |	"MH09e-ES-Patients with Patient Goals recorded (in FY)"|
  |"http://smartlifehealth.info/smh#f637ea5c-2048-4651-89e6-2a9ff72f5a76"|                                                      |	"MH10-ES-Patients with Medication Review recorded (in FY)"|
  |"http://smartlifehealth.info/smh#9f1cbbc3-7678-4dcb-8e4a-fd06d67ca91c"|                                                   |	"MH11a-ES-Patients with Serum Cholesterol AND on Anti Psychotics (in FY)"|
  |"http://smartlifehealth.info/smh#2b1621b3-d00e-4524-bae9-b99773b50524"|                                    |	"MH11b-ES-Serum Cholesterol NOT on Anti Psychotics aged over 35 yrs(in last 3yrs)"|
  |"http://smartlifehealth.info/smh#1247c225-dd56-4172-b0e3-b883ac26cca2"|                           |	"MH11c-ES-Patients aged under 35 yrs NOT on Anti Psychotics"                |
  |"http://smartlifehealth.info/smh#01bbdfba-e073-4f9b-b82a-66f829d10618"|                                                 |	"MH12a-ES-Patients with HbA1c/Blood Glucose AND on Anti-Psychotics (in FY)"|
  |"http://smartlifehealth.info/smh#50ec6ea8-a8a7-4d79-8bb5-49db6b2c16c7"||	"MH12b-ES-HbA1c/Blood Glucose NOT on Anti-Psychotics aged over 35 yrs(last 3yrs)"           |
  |"http://smartlifehealth.info/smh#880b8939-3a1a-4170-be05-4687b90cf7dd"||	"MH12c-ES-Patients aged under 3yrs NOT on Anti-Psychotics (last 3yrs)"                                          |
  |"http://smartlifehealth.info/smh#fd1b6c34-65ed-49f5-9e58-15299f1d781f"||	"MH13a-Lithium treatment with prescription in financial year"                                                   |
  |"http://smartlifehealth.info/smh#0d865a5b-ffbd-4a37-b020-00bacf737e01"||	"MH14-ES-Patients with Annual Review (1st Appt) recorded (in FY)"                                               |
  |"http://smartlifehealth.info/smh#94f42d6f-d5a8-409a-9410-25446722539c"||	"MH15b-ES-Patients with Annual Review and Follow Up not recorded on the same day"                               |
  |"http://smartlifehealth.info/smh#59760a68-73b8-4188-a235-096e6547ba83"||	"MH15-ES-Patients with Follow Up recorded (in FY)"                                                              |
  |"http://smartlifehealth.info/smh#40eee096-9be7-48d8-9731-bd2d973b5757"||	"MH08ab-ES-Patients advised about Cervical Cancer Screening (in FY)"|
  |"http://smartlifehealth.info/smh#d75b720c-0a0f-4b82-be8b-1915d58bca42"||	"MH08bb-ES-Patients advised about Breast Cancer Screening (in FY)"|
  |"http://smartlifehealth.info/smh#39fe4cf0-6527-4a7e-a3a5-e25cfecd2926"||	"MH08cb-NUM-Patients advised about Bowel Cancer Screening (in FY)"|
  |"http://smartlifehealth.info/smh#44a5d76f-9e3a-471b-8bdc-f038ad83f5bb"||	"MH11-ES-Patients with Serum Cholesterol recorded"|
  |"http://smartlifehealth.info/smh#bcb6e31d-73a7-47f6-ae54-cee699ace1f5"||	"MH12-ES-Patients with HbA1c/Blood Glucose recorded"|
  |"http://smartlifehealth.info/smh#eba2b911-6e39-4fe5-880e-a63d2a707159"||	"MH13ba-ES-Patients with Serum Lithium recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#3b748150-dafe-4523-b286-1120e990c538"||	"MH13bb-ES-Patients with eGFR recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#cd01edd1-1941-435d-8ccf-3432b963fd58"||	"MH13bc-ES-Patients with Serum TSH recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#ac424b22-9331-4201-9c2e-eb07d32fbd48"||	"MH15a-ES-Patients with Annual Review and Follow Up recorded on the same day"|
  |"http://smartlifehealth.info/smh#ce504822-e566-4c38-bdc3-58289b3b7a6e"||	"MH02-ES-Patients with Blood pressure recorded (in FY)"|
  |"http://smartlifehealth.info/smh#928ce18a-74f1-4ed5-8a9c-d21826cb88ec"||	"MH08a-ES-Patients with Cervical Cancer Screening advise OR not eligible (in FY)"|
  |"http://smartlifehealth.info/smh#79bd074d-cb2e-4526-8064-ca981ce50607"||	"MH08b-ES-Patients with Breast Cancer Screening advise OR not eligible (in FY)"|
  |"http://smartlifehealth.info/smh#7c75dc00-dad2-4c67-8811-3ae6db2bccdd"||	"MH08c-ES-Patients with Bowel Cancer Screening advise OR not eligible (in FY)"|
  |"http://smartlifehealth.info/smh#46a2d638-0578-47c5-afd9-ba5ef4c47a56"||	"MH13b-ES-Patients with Lithium Monitoring recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#a9ad89b3-ceb7-4c98-a360-3d974d07a5eb"||	"MH08-ES-Patients with appropriate Cancer Screening Prompts recorded (in FY)"|
  |"http://smartlifehealth.info/smh#0a31b0a6-0fb9-447f-a66f-d128ee460439"||	"MH13-ES-Patients with Lithium monitoring recorded twice OR not on Lithium(in FY)"|
  |"http://smartlifehealth.info/smh#b181fa70-f764-42a7-89f6-9ab2e142b612"||	"SMI Report"|
  |"http://smartlifehealth.info/smh#c82c4914-a4ef-448b-a041-baeaa400ab9c"||	"CCMI Report"|
  |"http://smartlifehealth.info/smh#31acffc2-586f-4423-9f00-e5ff95ecd42d"||	"DEP003-Patients with depression"|
  |"http://smartlifehealth.info/smh#a16bbed2-54bd-4061-87ee-0cfad9788408"||	"SMI Register - Patients with SMI"|
  |"http://smartlifehealth.info/smh#f2df5847-8309-40f6-a290-5725db3d055a"||	"EMH00-Patients with First Appt or Follow Up completed"|
  |"http://smartlifehealth.info/smh#24c41e4b-dea4-4fe2-9608-d6669df4de60"||	"Patients on CCMH Register (exc SMI Patients)"|
  |"http://smartlifehealth.info/smh#108a78c2-51c8-472d-937f-28c0ece899a8"||	"EMSMI00 -report"|
  |"http://smartlifehealth.info/smh#0f5b1dbc-2f44-4b1f-90d8-c30a28b80e2b"||	"EMMH00 -report"|
  |"http://smartlifehealth.info/smh#fea51d8b-3dfa-402e-966a-848ce2526d75"||	"EMCC00 -report"|
  |"http://smartlifehealth.info/smh#52a88755-fb1c-4a52-96d2-95c3fe5143f9"||	"Female Patients"|
  |"http://smartlifehealth.info/smh#75adbc4a-4cf4-4af6-8dfd-1e08a1c23367"||	"Male Patients"|
  |"http://smartlifehealth.info/smh#ebc8bff6-3a4b-40e0-b557-971ba508484f"||	"EMCC00a -report"|
  |"http://smartlifehealth.info/smh#f92878a5-0d07-493d-97b3-dad2e48e9d45"||	"EMCC00b -report"|
  |"http://smartlifehealth.info/smh#c6c129ca-30bd-4c46-89ff-c45a01bc1ff9"||	"CC00a-ES-Patients on Anti-Psychotics (in FY)"|
  |"http://smartlifehealth.info/smh#53f50bf6-cbb1-4c60-aa65-f656c9430e8e"||	"CC00b-ES-Patients with Personality Disorder"|
  |"http://smartlifehealth.info/smh#e42faea3-c1b1-4aab-bf9b-4ab79faf76e2"||	"DEP1_REG - Patients aged 18 or over with unresolved depression since April 2006"|
  |"http://smartlifehealth.info/smh#8a9f0848-7f6e-470c-8c15-0055b80e6cdd"||	"MH02a-Blood Pressure reading excluding home done in Financial Year"|
  |"http://smartlifehealth.info/smh#78562ce1-eceb-4e11-8907-400cb5570398"||	"MH02b-Blood Pressure reading done at Home in Financial Year"|
  |"http://smartlifehealth.info/smh#264b10c9-dac2-444e-900d-9427ca38f6c4"||	"MH1.1 - Psychosis, schizophrenia or bipolar diagnosis (pts in remission)"|
  |"http://smartlifehealth.info/smh#3bc2f621-5374-408a-8623-3a593fd95abf"||	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis"|
  |"http://smartlifehealth.info/smh#46eef911-c598-41de-a906-52121438de77"||	"MH2_REG - Lithium treatment with prescription in last 6 months"|
  |"http://smartlifehealth.info/smh#6fdf7979-b5a5-4350-bcd4-ff3f3ba0deda"||	"MH001 - Patients on the mental health register"|
  |"http://smartlifehealth.info/smh#97f1323e-60e7-4b19-a58e-306c262d7cf2"||	"SMI Register - Patients with SMI (MH001) deceased and deducted"|
  |"http://smartlifehealth.info/smh#76b0ed8f-063d-4b60-a6e8-4d667765e6df"||	"Patients on Serious Mental Illness (SMI) Register (excluding those in remission)"|
  |"http://smartlifehealth.info/smh#2175916b-c111-4b41-946d-bdc112ed9307"||	"NPT01a-ES-DQ-Patients with Near Patient Testing MISSING DMARD Medication"|
  |"http://smartlifehealth.info/smh#387c0047-0b88-4ed8-9936-d7dec7141683"||	"NPT01-ES-DQ-Patients on DMARD Medication MISSING Near Patient Testing Code"|
  |"http://smartlifehealth.info/smh#54009b78-4069-44c3-974e-66b4db4597d4"||	"Anonymised-Data Quality Reports-Finds missing Near Patient Testing Code"|
  |"http://smartlifehealth.info/smh#645b55d1-41d9-4ad8-a5f9-922f5d2a7996"||	"NHS Numbers-Data Quality Reports-Finds missing Near Patient Testing Code"|
  |"http://smartlifehealth.info/smh#40c022c5-96c5-43f2-a6f6-2cbf9f26cec2"||	"Anonymised-Data Quality Reports-Finds missing Near Patient Testing Code"|
  |"http://smartlifehealth.info/smh#92796c06-11e9-4b33-b5a2-d52c2cf91c5a"||	"NHS Numbers-Data Quality Reports-Finds missing Near Patient Testing Code"|
  |"http://smartlifehealth.info/smh#0c155919-3560-4946-a2f5-2ea9cfc9314d"||	"NPT01-ES-PAYMENT-Consultations for Near Patient Testing"|
  |"http://smartlifehealth.info/smh#306da9da-77a9-4742-9659-188a0ac8d4c6"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#b400a14b-bdc6-4dab-b838-e5a8879e0336"||	"EMNPT01 -report"|
  |"http://smartlifehealth.info/smh#e22ab2b4-abc4-4667-9074-4aa1a6389d82"||	"NPT01-ES-PAYMENT-Consultations for Near Patient Testing"|
  |"http://smartlifehealth.info/smh#d0c68565-8120-46ed-984b-cecc62437010"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#c1909798-ea6b-4688-a950-cfcd17d4e6de"||	"EMNPT01 -report"|
  |"http://smartlifehealth.info/smh#b9befe54-fc69-4883-a06f-c62d53f53ee3"||	"NPT01-ES-PAYMENT-Consultations for Near Patient Testing"|
  |"http://smartlifehealth.info/smh#7be5c89c-e145-4c95-896c-4a6d94b29aed"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#e892f039-9ee8-46d7-98cd-909cda9d5686"||	"EMNPT01 -report"|
  |"http://smartlifehealth.info/smh#5a79ce68-88d3-48b7-bab6-d7b3f2166509"||	"NPT01g-ES-Patients on Methotrexate Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#958640aa-d575-4aee-92ba-80b7649ef2cb"||	"NPT01h-ES-Patients on Mycophenolate mofetil & Mycophenolic Acid (in last 12m)"|
  |"http://smartlifehealth.info/smh#7e65efaf-5407-4fd3-bb42-c9d6b3795a16"||	"NPT01h-ES-Patients on Mycophenolate mofetil & Mycophenolic Acid (in last 4m)"|
  |"http://smartlifehealth.info/smh#b7ebc1c5-2190-463f-91f4-a0104258ac45"||	"NPT01i-ES-Patients on Penicillamine Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#a8fdbb6d-6dae-4846-ba6a-3c4083d14dcf"||	"NPT01i-ES-Patients on Penicillamine Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#a5c25a2a-b5c8-425b-9ac5-58a2d9cd8522"||	"NPT01j-ES-Patients on Sulfasalazine Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#43093034-6c08-4315-9f66-1df25d7590fd"||	"NPT01j-ES-Patients on Sulfasalazine Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#5923b798-077d-4716-88bf-bf2098e89249"||	"NPT01a-ES-Patients on Azathioprine Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#48b00c15-2de0-4bc7-942f-3749e1a10261"||	"NPT01a-ES-Patients on Azathioprine Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#35797bed-f187-484a-8418-890c48d0374a"||	"NPT01b-ES-Patients on Ciclosporin Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#998a407c-348c-4864-8795-fedf7819d6c7"||	"NPT01b-ES-Patients on Ciclosporin Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#97830088-9c34-4552-b033-30456b7964ba"||	"NPT01c-ES-Patients on Hydroxycarbamide Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#cafa1969-4389-4484-8f93-3f2a851c75bc"||	"NPT01c-ES-Patients on Hydroxycarbamide Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#b4dbd3a3-1f6a-475c-9157-75c85da37649"||	"NPT01d-ES-Patients on Hydroxychloroquine Sulfate Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#cefca05d-65d8-4339-9e9e-796d9d829ac8"||	"NPT01d-ES-Patients on Hydroxychloroquine Sulfate Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#1bbb7dc2-dc98-4853-844c-ba5da0c7d4b9"||	"NPT01e-ES-Patients on Leflunomide Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#2baa7d7d-74c9-4d57-a8bd-96f0a805178f"||	"NPT01e-ES-Patients on Leflunomide Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#9b64af1a-9677-4cd9-93a0-2b0dbb264dc4"||	"NPT01f-ES-Patients on Mercaptopurine Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#40522b27-ffab-4c14-9453-b93ddf048c0b"||	"NPT01f-ES-Patients on Mercaptopurine Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#f9c85a14-7106-4df9-bdb1-f55ce436454e"||	"NPT01g-ES-Patients on Methotrexate Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#a967532e-0222-4997-a9bd-1e2a68441a0e"||	"NPT01k-ES-Patients on DMARD Medication (in last 12m)"|
  |"http://smartlifehealth.info/smh#c4d1f53d-41f0-4a58-acde-c8aa1473a52d"||	"NPT01k-ES-Patients on DMARD Medication (in last 4m)"|
  |"http://smartlifehealth.info/smh#8bdc4c12-ffb5-4dad-801e-5173299178da"||	"PHL02-ES-Housebound Patients with Phlebotomy WITHOUT Home Visit"|
  |"http://smartlifehealth.info/smh#15bbda12-e834-47a5-956e-3555f93fc99a"||	"Anonymised - Data Quality Report - POTENTIAL missing Home Visit Code"|
  |"http://smartlifehealth.info/smh#2d82ef1e-c017-40fb-a19f-12f7943b5f5f"||	"NHS Numbers - Data Quality Report - POTENTIAL missing Home Visit Code"|
  |"http://smartlifehealth.info/smh#20c5693b-2fc0-4235-8341-b42eda122a2a"||	"PHL01-ES-PAYMENT-Number of Blood Samples taken"|
  |"http://smartlifehealth.info/smh#71e05c62-f643-4920-84b9-020d44cdf9c6"||	"PHL02-ES-PAYMENT-Home Visits for Blood Tests"|
  |"http://smartlifehealth.info/smh#d44b6691-805d-4de1-ad08-856e23cbd301"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#545559b3-b51b-4083-a356-ddac69dd8d29"||	"EMPHL01 -report"|
  |"http://smartlifehealth.info/smh#09a90442-5127-4cce-866b-525c93e4fa01"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#60774a55-f199-4c63-81ed-f0fd3c726a81"||	"EMPHL02 -report"|
  |"http://smartlifehealth.info/smh#18ad280e-20d5-4589-ad0f-52301b0de406"||	"PHL01-ES-PAYMENT-Number of Blood Samples taken"|
  |"http://smartlifehealth.info/smh#6967b22b-6215-4621-ba25-a32bd228633b"||	"PHL02-ES-PAYMENT-Home Visits for Blood Tests"|
  |"http://smartlifehealth.info/smh#84ad27f9-2222-41a5-bfc2-9d81e77b65b2"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#20681f9c-6f1d-4be5-8d47-c9263989cd18"||	"EMPHL01 -report"|
  |"http://smartlifehealth.info/smh#882d7ba9-3ea3-436e-b1ca-57612416428e"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#3f70134b-5bec-41d9-9722-28a5c930dbb0"||	"EMPHL02 -report"|
  |"http://smartlifehealth.info/smh#2ddba177-74e3-4570-b80c-52902b9b20d2"||	"PHL01-ES-PAYMENT-Number of Blood Samples taken"|
  |"http://smartlifehealth.info/smh#ad0d1337-9dde-442c-91a7-3f380750de1d"||	"PHL02-ES-PAYMENT-Home Visits for Blood Tests"|
  |"http://smartlifehealth.info/smh#cc87f61f-3e38-4e9c-a68b-436dabf87b85"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#0641e6b0-80e3-4ee2-b7bd-4281eb3aba79"||	"EMPHL01 -report"|
  |"http://smartlifehealth.info/smh#11a6a7a1-c200-4f0f-b667-4c0d1e14f4f8"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#df581be6-ed6d-4b59-a135-9102a03f8db0"||	"EMPHL02 -report"|
  |"http://smartlifehealth.info/smh#d9255d83-db06-472a-b32a-ee5f8c4afa1f"||	"PHL01-ES-PAYMENT-Number of Blood Samples taken"|
  |"http://smartlifehealth.info/smh#031358d7-a9e9-4fb2-bc9f-f7bbebf0b3c0"||	"PHL02-ES-PAYMENT-Home Visits for Blood Tests"|
  |"http://smartlifehealth.info/smh#d3a34c8c-2f5a-4987-af4a-7e390865fcce"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#f7bc7f22-b9af-4a99-8f35-0972f1b93710"||	"EMPHL01 -report"|
  |"http://smartlifehealth.info/smh#23b7d7e4-3ba9-417c-9010-b09aa4d834a9"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#58ec813a-1b0a-4df6-af8b-dfaab090723b"||	"EMPHL02 -report"|
  |"http://smartlifehealth.info/smh#a4083fba-7bd7-4e35-bf60-9f9ed2e11e11"||	"PHL01-ES-PAYMENT-Number of Blood Samples taken"|
  |"http://smartlifehealth.info/smh#ef8caf90-6481-4ce2-8841-857370374bd8"||	"PHL02-ES-PAYMENT-Home Visits for Blood Tests"|
  |"http://smartlifehealth.info/smh#fd4a07b9-449c-4bf3-ac88-721cd969d9be"||	"Female"|
  |"http://smartlifehealth.info/smh#359f29fd-e91a-435e-a36a-e36e63762b9c"||	"Male"|
  |"http://smartlifehealth.info/smh#d192ab12-d17c-4a41-9395-78cdc015617d"||	"EMPHL02 -report"|
  |"http://smartlifehealth.info/smh#2af1e464-25c2-47dc-b75d-c353fa629c23"||	"Age <= 10"|
  |"http://smartlifehealth.info/smh#cafeaf0c-d118-4c34-aa44-2acca57ca450"||	"Age > 10 and <= 20"|
  |"http://smartlifehealth.info/smh#386f0ef6-f649-4956-9e21-6cc86efc156a"||	"Age > 20 and <= 30"|
  |"http://smartlifehealth.info/smh#02fc330c-a2f6-4e4d-aa95-927b5b1ae026"||	"Age > 30 and <= 40"|
  |"http://smartlifehealth.info/smh#e0f494e1-efab-492e-bcd0-dc0b8022ff9e"||	"Age > 40 and <= 50"|
  |"http://smartlifehealth.info/smh#da8dbf9f-d9a5-480e-914c-0faf52285c04"||	"Age > 50 and <= 60"|
  |"http://smartlifehealth.info/smh#c2ce2094-53f2-4884-baa4-0eb28b4f6b2a"||	"Age > 60 and <= 70"|
  |"http://smartlifehealth.info/smh#90876a4a-1da0-4b26-92de-4a5540f2b1fb"||	"Age > 70 and <= 80"|
  |"http://smartlifehealth.info/smh#f689b841-d799-48d6-9b7e-278dcf7261ed"||	"Age > 80 and <= 90"|
  |"http://smartlifehealth.info/smh#fdba4ae6-72ba-4c44-8abf-06a4e6ef0525"||	"Age > 90"|
  |"http://smartlifehealth.info/smh#0cf4333d-2480-4f87-b556-4eba7ed9daf5"||	"Age <= 10"|
  |"http://smartlifehealth.info/smh#78e73fd7-6055-40d9-9f34-86f067f0a063"||	"Age > 10 and <= 20"|
  |"http://smartlifehealth.info/smh#c0b494cb-9233-40bf-ae3c-835c91a3e9ff"||	"Age > 20 and <= 30"|
  |"http://smartlifehealth.info/smh#e23344da-3437-40c7-ad06-9ba2756a5c74"||	"Age > 30 and <= 40"|
  |"http://smartlifehealth.info/smh#3aefacb3-2eeb-4b7f-b60f-bf0436b9dfe7"||	"Age > 40 and <= 50"|
  |"http://smartlifehealth.info/smh#c1cc636c-3670-4d0d-9e12-2525b367036e"||	"Age > 50 and <= 60"|
  |"http://smartlifehealth.info/smh#db370558-2ea3-464a-8180-15ab4dc99110"||	"Age > 60 and <= 70"|
  |"http://smartlifehealth.info/smh#02d39e93-db43-4374-b990-8b4e9cae0316"||	"Age > 70 and <= 80"|
  |"http://smartlifehealth.info/smh#3ab47d66-80ee-47cf-9047-2ee927b64db0"||	"Age > 80 and <= 90"|
  |"http://smartlifehealth.info/smh#44b6b674-c7dd-4486-9c1c-8176c9b57eac"||	"Age > 90"|
  |"http://smartlifehealth.info/smh#9c8dc6ea-fb9e-43c6-9b6a-86a1524d38ba"||	"EMPHL01a -report"|
  |"http://smartlifehealth.info/smh#a009dfc9-ec72-4867-9079-008790f5ad86"||	"EMPHL01b -report"|
  |"http://smartlifehealth.info/smh#dabf082b-9644-47d7-91ef-641d37c1cc0d"||	"EMPHL01c -report"|
  |"http://smartlifehealth.info/smh#c1dda0de-4a86-4c49-ae20-d76d276b21e8"||	"EMPHL01d -report"|
  |"http://smartlifehealth.info/smh#86e7eddc-ecec-4fc1-b5fa-0a3ba4df1720"||	"EMPHL01e -report"|
  |"http://smartlifehealth.info/smh#f9284a5d-cd89-4e2a-92bd-569769e359b9"||	"EMPHL01f -report"|
  |"http://smartlifehealth.info/smh#1c07a951-adcc-4388-a855-8e02a12b9364"||	"EMPHL01g -report"|
  |"http://smartlifehealth.info/smh#7de9b0d5-bdc6-42d8-8edf-5a4173d16e50"||	"EMPHL01h -report"|
  |"http://smartlifehealth.info/smh#efa8085e-b3f5-4179-9b0e-ff8ebb2ba9c6"||	"EMPHL01i -report"|
  |"http://smartlifehealth.info/smh#279bff35-d015-44b8-8c37-eaeeb400942f"||	"EMPHL01j -report"|
  |"http://smartlifehealth.info/smh#5d989192-f82b-4d70-bc79-0f091c444470"||	"EMPHL01k -report"|
  |"http://smartlifehealth.info/smh#54c392d2-70e6-4b26-a885-c3def1f02d63"||	"EMPHL01l -report"|
  |"http://smartlifehealth.info/smh#d9791172-f4a0-45fd-a1bc-3d605f8d1fb0"||	"EMPHL01m -report"|
  |"http://smartlifehealth.info/smh#b295e544-948f-40b9-915a-fc266aa44dab"||	"EMPHL01n -report"|
  |"http://smartlifehealth.info/smh#dac8ccf5-5949-4ef4-b73c-9ba318443882"||	"EMPHL01o -report"|
  |"http://smartlifehealth.info/smh#03b1a610-42d5-412a-bdd8-364022d89c1c"||	"EMPHL01p -report"|
  |"http://smartlifehealth.info/smh#d066458d-be07-449f-b906-a15f0ecbcd9e"||	"EMPHL01q -report"|
  |"http://smartlifehealth.info/smh#630f0e39-3bd9-4a89-8e79-0a57c8aba00d"||	"EMPHL01r -report"|
  |"http://smartlifehealth.info/smh#fd7c2b77-50a2-4150-8e17-bddeabf6197e"||	"EMPHL01s -report"                                                                      |
  |"http://smartlifehealth.info/smh#8e8abdd9-9f42-4a77-b044-0fbc5af7f862"||	"EMPHL01t -report"|
  |"http://smartlifehealth.info/smh#f0515a9f-1b59-4f5b-acc2-938cb1ab032e"||	"PPHL02-ES-Housebound Patients with Phlebotomy WITHOUT Home Visit"|
  |"http://smartlifehealth.info/smh#47d14e7f-e406-4e6d-bed0-06ae17d6e636"||	"Anonymised - Data Quality Report - POTENTIAL missing Home Visit Code"|
  |"http://smartlifehealth.info/smh#a062e466-718a-4e36-8dc7-d57027c28937"||	"NHS Numbers - Data Quality Report - POTENTIAL missing Home Visit Code"|
  |"http://smartlifehealth.info/smh#75fddb9c-b4ff-49f7-a72b-f1aa07fbeb39"||	"PPHL02  PAYMENT  Home Visits for Blood Tests"|
  |"http://smartlifehealth.info/smh#2f2ccf1b-9a03-4f86-9711-50819d4c286b"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#0fa0728b-6851-4d66-85e4-5dd2dda3cae6"||	"EMPPHL02 -report"|
  |"http://smartlifehealth.info/smh#57836be7-1fcc-49d9-9e64-366a50e72541"||	"PPHL01a  PAYMENT  Aged 2-4 years  Blood Samples taken"|
  |"http://smartlifehealth.info/smh#3b489972-2eb5-42ba-b3df-8c0f548a08fd"||	"PPHL01b  PAYMENT  Aged 5-13 years  Blood Samples taken"|
  |"http://smartlifehealth.info/smh#057aa886-7db4-4778-971e-8a285a886d25"||	"PPHL03  PAYMENT  Blood Samples taken on LD Register"|
  |"http://smartlifehealth.info/smh#e29cb0a4-7afe-47d8-bbc0-e4bcaa6704f4"||	"EMPPHL03 -report"|
  |"http://smartlifehealth.info/smh#55eaef0a-de60-4752-836a-b9990ed5de43"||	"Activity Level Report for Payment - Learning Disabilities"|
  |"http://smartlifehealth.info/smh#76bce12e-5082-4c71-b052-772169ba896a"||	"EMPPHL01b -report"|
  |"http://smartlifehealth.info/smh#2b741024-5b94-4b3b-a536-16c70e0ed71a"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#73c77ef1-5069-48c4-854d-0eee27e507c2"||	"EMPPHL01a -report"|
  |"http://smartlifehealth.info/smh#8dae10ab-f676-4d2e-af68-735f86797cb5"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#812d60d9-4dab-4824-86e9-a5084893478b"||	"PPHL02  PAYMENT  Home Visits for Blood Tests"|
  |"http://smartlifehealth.info/smh#d246287a-57ca-4b62-a4c4-86d613f4c7ed"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#0f330156-9894-4cb3-b689-8ac3dc8a2651"|                                                                    |	"EMPPHL02 -report"                                                  |
  |"http://smartlifehealth.info/smh#2cb97bfe-5126-4f25-a025-b288bce5abe7"|                                    |	"PPHL01a  PAYMENT  Aged 2-4 years  Blood Samples taken"                                                               |
  |"http://smartlifehealth.info/smh#6b8d349a-4144-4ee4-9fb5-89a393d3d470"||	"PPHL01b  PAYMENT  Aged 5-13 years  Blood Samples taken"|
  |"http://smartlifehealth.info/smh#dd513315-33d5-4e3e-979a-211b42692182"||	"PPHL03  PAYMENT  Blood Samples taken on LD Register"                                               |
  |"http://smartlifehealth.info/smh#19a12bec-fd27-4b77-8a46-649eac9bfa28"|                                                                      |	"Activity Level Report for Payment"                         |
  |"http://smartlifehealth.info/smh#00e02fda-a65d-4abd-89a8-db35cb0a2751"||	"EMPPHL01a -report"                    |
  |"http://smartlifehealth.info/smh#1e4cf4d8-42af-478f-b48d-4d9293f00b72"||	"Activity Level Report for Payment"                       |
  |"http://smartlifehealth.info/smh#503e53b0-1b3a-4701-b613-ec990cad5c1f"|                     |	"EMPPHL01b -report"|
  |"http://smartlifehealth.info/smh#b848d60f-7505-4824-b679-f1fc1e4957df"||	"Activity Level Report for Payment - Learning Disabilities"|
  |"http://smartlifehealth.info/smh#e1f8ca60-1f62-48c6-8a98-ce3da1e0655e"||	"EMPPHL03 -report"|
  |"http://smartlifehealth.info/smh#8f715ff1-e118-4303-b92c-fe775d32fe0f"||	"PPHL02  PAYMENT  Home Visits for Blood Tests"|
  |"http://smartlifehealth.info/smh#6a1a01d3-c7b2-4ce0-b5b9-0059d37035dc"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#3f9b8f9f-f27b-4df4-8988-7f988b1cd15e"||	"EMPPHL02 -report"                                 |
  |"http://smartlifehealth.info/smh#bfaf97ce-0a69-44d0-860c-b211cd5b43af"|                           |	"PPHL01a  PAYMENT  Aged 2-4 years  Blood Samples taken"                                      |
  |"http://smartlifehealth.info/smh#b254c604-ae74-4118-a99e-82f75d63bb50"||	"PPHL01b  PAYMENT  Aged 5-13 years  Blood Samples taken"|
  |"http://smartlifehealth.info/smh#87bce267-07c5-48b3-9245-203c83089c91"|                                                                    |	"PPHL03  PAYMENT  Blood Samples taken on LD Register"|
  |"http://smartlifehealth.info/smh#09f6ad7c-51ee-47fe-bd31-bc724dd21c49"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#5c171592-6186-449f-9f0b-c5ea355ac264"||	"EMPPHL01a -report"                    |
  |"http://smartlifehealth.info/smh#cf55bc89-221b-426f-a378-5ae5200875c3"|   |	"Activity Level Report for Payment"                                              |
  |"http://smartlifehealth.info/smh#df8d8441-c6a7-4818-b555-7532f99e2444"||	"EMPPHL01b -report"                                                              |
  |"http://smartlifehealth.info/smh#17d41982-d46e-4736-8dbd-9aeee6abf887"|        |	"Activity Level Report for Payment - Learning Disabilities"|
  |"http://smartlifehealth.info/smh#4e3bc4e4-55cd-4339-9644-a52a71cdde29"|                                                                   |	"EMPPHL03 -report"|
  |"http://smartlifehealth.info/smh#4ec90265-db3c-42c2-a432-6aa97d5cdd4f"|                                    |	"PPHL02  PAYMENT  Home Visits for Blood Tests"                                              |
  |"http://smartlifehealth.info/smh#b8caec94-05a8-42b3-962d-1358e1ae2a21"|         |	"Activity Level Report for Payment"                                          |
  |"http://smartlifehealth.info/smh#c6ba4b4c-73c3-459d-9650-5fec9f20400a"|                 |	"EMPPHL02 -report"    |
  |"http://smartlifehealth.info/smh#cb9c53a7-ef04-4a44-9ab1-5f754e7b8d7e"|                |	"PPHL01a  PAYMENT  Aged 2-4 years  Blood Samples taken"|
  |"http://smartlifehealth.info/smh#a96aac20-13a1-40d5-8d62-90c979a29c97"||	"PPHL01b  PAYMENT  Aged 5-13 years  Blood Samples taken"|
  |"http://smartlifehealth.info/smh#4196331a-a337-4a7c-a231-1be86bca89b3"|                                  |	"PPHL03  PAYMENT  Blood Samples taken on LD Register"|
  |"http://smartlifehealth.info/smh#892381db-4f7a-4fbe-b137-e5be0328452c"|     |	"Activity Level Report for Payment"                                                      |
  |"http://smartlifehealth.info/smh#86b72c5f-f446-4d0b-ab03-a4befda0dfc1"||	"EMPPHL01a -report"|
  |"http://smartlifehealth.info/smh#06990db8-0d90-49a4-9622-3e23d8ff8f8b"|                                          |	"Activity Level Report for Payment"                                                         |
  |"http://smartlifehealth.info/smh#0f84e6a8-5568-49c1-9790-53324d1245e8"|              |	"EMPPHL01b -report"               |
  |"http://smartlifehealth.info/smh#3907f2a7-c045-4697-b434-a1455a7f48ec"|                             |	"Activity Level Report for Payment - Learning Disabilities"|
  |"http://smartlifehealth.info/smh#123f74d2-c1be-47e5-a20b-967b5f266ae2"||	"EMPPHL03 -report"|
  |"http://smartlifehealth.info/smh#7dfe006a-3c76-42e2-875f-d927c670f364"|                         |	"LD004 - Patients on the learning disabilities register (Deceased and deducted)"|
  |"http://smartlifehealth.info/smh#06ec4d4b-ac46-46bd-a20c-f9f76de3d8a9"||	"*RESP01D-ES-DEN-Patients in COPD OPTIMISE Cohort"|
  |"http://smartlifehealth.info/smh#b718212f-dfbb-4baf-9acd-23a027f1fea3"||	"RESP01Na-Optimise Treatment (in Financial Year)"|
  |"http://smartlifehealth.info/smh#e2a31a44-8406-4d3c-b864-e31ff8e1297d"||	"RESP01Nb-Pulmonary Rehab (in Financial Year)"|
  |"http://smartlifehealth.info/smh#c5d011be-5645-4d2e-88fc-d7aa36d83613"||	"RESP01Nc-Tobacco dependence services (in Financial Year)"                               |
  |"http://smartlifehealth.info/smh#8693091b-031b-4832-b141-627d387d09b7"||	"RESP01Nd-Inhaler Technique (in Financial Year)"|
  |"http://smartlifehealth.info/smh#0ffd5252-7e07-45d1-aced-e8a928ca8a33"||	"RESP01Ne-Offered or Administered Vaccine (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ddc37b8f-d3dd-4c1c-934d-3d8290c2a3dc"||	"RESP01Nf-Physical Activity (in Financial Year)"|
  |"http://smartlifehealth.info/smh#cf2bb2ec-a2c9-4144-9c19-eea69a5a5837"||	"RESP01Ng-Support for Psychosocial wellbeing (in Financial Year)"|
  |"http://smartlifehealth.info/smh#d43a4835-f542-42ce-980f-71dc610de466"|                                |	"RESP01Nh-Education and self-management (in Financial Year)"                                                       |
  |"http://smartlifehealth.info/smh#ff7b41db-b45c-4569-8a83-da475181c0e9"|              |	"EMRESP01D -report"       |
  |"http://smartlifehealth.info/smh#7676d901-ea14-499d-9061-ce6c8f9e8d11"|                     |	"RESP01-NHS NUMBERS-8 Care Processes -report"          |
  |"http://smartlifehealth.info/smh#2b3b96f3-36d1-404c-8896-6742807c6415"|                               |	"*RESP01N-ES-NUM-8 Care Processes (in Financial Year)" |
  |"http://smartlifehealth.info/smh#52b41bd4-6819-4416-8b1b-6ebafc9a40b7"||	"RESP02D-ES-DEN-Patients in Asthma or COPD registers"                                                             |
  |"http://smartlifehealth.info/smh#199f2fae-ceea-4787-85fe-92e5b0ea0ea2"||	"EMRESP02D -report"                                              |
  |"http://smartlifehealth.info/smh#d4ea4ec7-8a56-4468-af11-c47245302924"||	"RESP02N-ES-NUM-Patients with Inhaler Technique recorded (in Financial Yr)"|
  |"http://smartlifehealth.info/smh#11dcd494-a564-4c8f-b45b-384cbe8e53ed"||	"RESP02-NHS NUMBERS-Patient level report"|
  |"http://smartlifehealth.info/smh#5f38069e-9d00-4d15-99fa-bdd78cc512b8"||	"RESP03D-ES-DEN-Asthma patients aged 12 and over diagnosed in last 12 months"                    |
  |"http://smartlifehealth.info/smh#4e199a1f-f2d1-40d2-9577-849fc1a6887c"|                                                                    |	"EMRESP03D -report"|
  |"http://smartlifehealth.info/smh#f85af29c-8c68-4f06-a3d7-33aa41e9bce7"|                                                          |	"RESP03N-ES-NUM-Initiated on MART or AIR Inhaled Therapy"|
  |"http://smartlifehealth.info/smh#13770c2f-4de1-490f-aae8-f8cd34cb9834"|                        |	"RESP03-NHS NUMBERS-Activity level report"|
  |"http://smartlifehealth.info/smh#7ed4289f-2b53-4f38-8454-3aa3a4185f58"|                            |	"AST005 - Patients on the asthma register"|
  |"http://smartlifehealth.info/smh#3d153492-8d72-4c67-9a0c-f259a9091185"||	"COPD015a-Earliest unresolved COPD diagnosis"|
  |"http://smartlifehealth.info/smh#d0509695-f10e-41c1-9ba9-e46ebe781dee"|                        |	"COPD015b-Unresolved COPD Diagnosis, spirometry below 0.7 after registration"      |
  |"http://smartlifehealth.info/smh#34e77d2b-bbe2-4f56-a716-b5959e7c69c0"||	"COPD01-Patients diagnosed with COPD"                                   |
  |"http://smartlifehealth.info/smh#c051fc1e-6109-4753-bab8-61a18632c536"|                               |	"RESP01D-Antibiotics and prednisolone 5mg tablets issued same day in last 12m"|
  |"http://smartlifehealth.info/smh#acaf9998-aff2-453a-9f7a-1d41d46230ad"||	"RESP01D-Antibiotics and prednisolone 5mg tablets issued same day(after 01/04/24)"|
  |"http://smartlifehealth.info/smh#ef24f3cd-2394-4e35-8403-f9476e9bf32e"|                  |	"AST005 - Diagnosed with asthma in financial year and aged 12 and over"|
  |"http://smartlifehealth.info/smh#db0911f7-16b2-4cb1-b12d-e9c4b861805d"||	"COPD015 - Patients on the COPD register"|
  |"http://smartlifehealth.info/smh#8d6359c7-8889-4cf8-b0b8-de2ed90c81ec"|                                                                  |	"R01-ES-Ring Pessary Consultations MISSING Enhanced Services Admin Code"    |
  |"http://smartlifehealth.info/smh#b20ca490-f65d-47ed-800c-42a8005baa35"||	"Anonymised - DQ Report - POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#6f9e7018-2683-42ef-b3f8-3be75fa219a5"|                                 |	"NHS Numbers - DQ Report - POTENTIAL missing Enhanced Services Admin code" |
  |"http://smartlifehealth.info/smh#50c2ed70-aa39-4ab4-8c4c-f4dadcb62ac1"||	"R01-ES-PAYMENT-Consultations for Ring Pessary recorded"|
  |"http://smartlifehealth.info/smh#c8d6ff47-06da-4e74-8b9c-1a24e87e8acd"|                                                |	"Activity Level Report for payment"                                          |
  |"http://smartlifehealth.info/smh#3afea8ba-3803-4fc6-8202-4b3225286eda"|                    |	"EMR01 -report"|
  |"http://smartlifehealth.info/smh#c0517705-bd91-4d25-bc92-a77a4fe9c6bc"|                   |	"R01-ES-PAYMENT-Consultations for Ring Pessary recorded"|
  |"http://smartlifehealth.info/smh#25528b28-37c7-419f-80ca-672eea5e33e3"||	"Activity Level Report for payment"                                                   |
  |"http://smartlifehealth.info/smh#0589fc8a-c731-4f00-a5bf-96b4927dd4c1"||	"EMR01 -report"                                                                      |
  |"http://smartlifehealth.info/smh#62f425ac-bc56-4a68-9d20-89c895062501"||	"R01-ES-PAYMENT-Consultations for Ring Pessary recorded"             |
  |"http://smartlifehealth.info/smh#59469c31-5412-4327-a909-634ee886d171"||	"Activity Level Report for payment"                   |
  |"http://smartlifehealth.info/smh#aea6f5be-e835-4900-950f-5b429cea1e8b"||	"EMR01 -report"                             |
  |"http://smartlifehealth.info/smh#ee431d96-5ba7-4caf-80dc-d50f5698dab3"||	"R01-ES-PAYMENT-Consultations for Ring Pessary recorded"|
  |"http://smartlifehealth.info/smh#273e95b4-1609-41a8-9334-436031be4904"||	"Activity Level Report for payment"|
  |"http://smartlifehealth.info/smh#3d3558ff-c9e5-4b73-be9c-1496a59d6197"||	"EMR01 -report"   |
  |"http://smartlifehealth.info/smh#441341ac-c2c4-4490-87f1-3e02c0e26ab0"||	"SG01-ES-PAYMENT-Safeguarding report with MDS completed"|
  |"http://smartlifehealth.info/smh#34f5eab0-c695-48b2-bcc7-5af995251650"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#7a64ad4e-a601-4ef8-ad48-a98d6977a4f3"||	"EMSG01 -report"   |
  |"http://smartlifehealth.info/smh#5f463d69-22b3-41ba-a0d6-88d275a2eba1"|                              |	"SG01-ES-PAYMENT-Safeguarding report with MDS completed"|
  |"http://smartlifehealth.info/smh#57793513-9414-42d9-947a-31092c901790"|                 |	"Activity Level Report for Payment"                                                                |
  |"http://smartlifehealth.info/smh#40c06aaf-6473-43c7-9210-b03789f87545"|                            |	"EMSG01 -report"|
  |"http://smartlifehealth.info/smh#d59a4fb8-c227-4529-a478-398a8a2abbdf"|                  |	"SG01-ES-PAYMENT-Safeguarding report with MDS completed"                                 |
  |"http://smartlifehealth.info/smh#cf478a8e-6157-49dd-b939-9575746cb4ec"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#5bf1f97f-3d29-40b0-b7f5-3526393e5313"||	"EMSG01 -report"                           |
  |"http://smartlifehealth.info/smh#8132aad2-4456-4118-86b2-343597e91282"|     |	"SG01-ES-PAYMENT-Safeguarding report with MDS completed"|
  |"http://smartlifehealth.info/smh#fc50dac2-6efe-496e-9834-416299064de4"||	"Activity Level Report for Payment"                   |
  |"http://smartlifehealth.info/smh#d509d404-b4c3-4b76-9305-ea8c5a8df9d3"||	"EMSG01 -report"                                                |
  |"http://smartlifehealth.info/smh#cb5fb6b7-0fb6-4434-8ef4-a48fb872c044"|     |	"SP01-ES-Spirometry MISSING Enhanced Services Admin Code"                 |
  |"http://smartlifehealth.info/smh#916bf9fc-c927-47d3-a0a7-351fe3d02ad5"||	"Anonymised - DQ Report - POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#35e3d34e-2fef-4e92-833e-8abf4a7cf972"|                                                         |	"NHS Numbers - DQ Report - POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#ac502ff2-80b2-416e-aeed-ac2c3fd20250"|                                                      |	"SP01-ES-PAYMENT-Number of Consultation Utilised for Spirometry"                                                     |
  |"http://smartlifehealth.info/smh#97030e9b-f14a-4222-a6db-cbe1cb1dab4d"||	"Activity Level Report for Payment"                                       |
  |"http://smartlifehealth.info/smh#c13e550f-bc01-4311-96ca-820793155181"||	"EMSP01 -report"                                                             |
  |"http://smartlifehealth.info/smh#ce56a737-3587-4b07-9ebe-7aa5c1d9bac7"||	"SP01-ES-PAYMENT-Number of Consultation Utilised for Spirometry"   |
  |"http://smartlifehealth.info/smh#374ebccd-797a-40b7-a958-060769638c23"||	"Activity Level Report for Payment"                            |
  |"http://smartlifehealth.info/smh#825e701a-9a2f-4d34-9dab-be18db9f867c"|                                                                  |	"EMSP01 -report"                                                               |
  |"http://smartlifehealth.info/smh#b4fe075f-c582-4efc-baf4-b5973d27805a"| |	"SP01-ES-PAYMENT-Number of Consultation Utilised for Spirometry"                                                        |
  |"http://smartlifehealth.info/smh#246c18fa-6377-4610-b7e9-9cf0b9fd3782"|                                             |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#dcef5b5e-7dfe-42b0-a7a7-f136e26187e7"||	"EMSP01 -report"                   |
  |"http://smartlifehealth.info/smh#eabe0fc6-2027-404f-8ee1-95ecec06e133"||	"SP01-ES-PAYMENT-Number of Consultation Utilised for Spirometry"|
  |"http://smartlifehealth.info/smh#c67ffa85-79d1-4143-b508-1ef32e36a38a"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#87159800-ff56-4bd6-9d4e-9181113182e3"||	"EMSP01 -report"|
  |"http://smartlifehealth.info/smh#8ac24e57-4a43-4fa1-b649-73c39f5d56df"|                                                      |	"SPIRO02b-Respiratory Hub Appointments"|
  |"http://smartlifehealth.info/smh#8af1357e-6b6b-488a-ace0-4720cc2a2608"|                                  |	"SPIRO03a-Patients diagnosed with asthma"                                                                      |
  |"http://smartlifehealth.info/smh#5898fc37-23ea-4a5d-a94a-1711d384cbb7"|                                       |	"SPIRO03b-Patients diagnosed with COPD"                                 |
  |"http://smartlifehealth.info/smh#15396676-2b60-4598-ab16-e74da69af7f6"|                               |	"SPIRO04b-FeNo Activity (Last Month)"                                            |
  |"http://smartlifehealth.info/smh#27bb4636-db1b-458c-97c4-25196c978511"|                         |	"SPIRO05c-First diagnosis Asthma in FY"|
  |"http://smartlifehealth.info/smh#3401d968-f564-4010-a469-3e99606a6258"|              |	"SPIRO05d-First diagnosis COPD in FY"                                                                      |
  |"http://smartlifehealth.info/smh#5837f7c2-d4bf-4297-a30e-6de866c11473"||	"SPIRO02b -report"|
  |"http://smartlifehealth.info/smh#845d6975-3696-44a1-9ab6-a62dd0d3c65d"||	"SPIRO03c -report"|
  |"http://smartlifehealth.info/smh#c6e92b8e-d521-4763-af7f-aaea1387c116"|         |	"SPIRO03d -report"|
  |"http://smartlifehealth.info/smh#b1fbdaef-5428-4b9c-9e7e-a5fa9683262e"||	"SPIRO04b -report"|
  |"http://smartlifehealth.info/smh#e66cffae-7b20-459c-bb16-ec51a6cec107"|                                        |	"SPIRO05c -report"                     |
  |"http://smartlifehealth.info/smh#e1c92403-5afa-40db-93f6-f0e39ef128a3"|                                   |	"SPIRO05d -report"                      |
  |"http://smartlifehealth.info/smh#4b337b24-25f1-45d6-acee-4edb408bc6be"|                |	"W01-ES-Wound Care MISSING Enhanced Services Admin Code"|
  |"http://smartlifehealth.info/smh#d7cead8e-951c-4b99-9007-4ec54028251a"|                                                                   |	"W02-ES-Wound Care Patients - Wound Care WITHOUT Home Visit"                    |
  |"http://smartlifehealth.info/smh#fc876a5e-1b70-492f-a722-726d9c7acc54"||	"Anonymised - DQ Report - POTENTIAL missing Enhanced Services Admin code"                              |
  |"http://smartlifehealth.info/smh#f6aa7cb5-7e52-4ab9-ad62-303a4a4ba26c"||	"NHS Numbers - DQ Report - POTENTIAL missing Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#15defbfb-ba59-440a-adb4-1d7da0af724d"|                               |	"Anonymised Identifier - Data Quality Report - missing POTENTIAL Home Visit Code"                      |
  |"http://smartlifehealth.info/smh#b7b644d3-8269-48e2-bffc-743f834ecbc8"|                                          |	"NHS Numbers - Data Quality Report - missing POTENTIAL Home Visit Code"             |
  |"http://smartlifehealth.info/smh#a8b2ed19-148e-4beb-9124-ae302c0aab6f"||	"W01-ES-PAYMENT-Number of Consultation Utilised for Wound Care"                          |
  |"http://smartlifehealth.info/smh#3d0b6b52-25d0-46fa-a5ca-e431be6fb5a4"|                                                                      |	"W02-ES-PAYMENT- Number of Home Visits for Wound Care"                                             |
  |"http://smartlifehealth.info/smh#3ee96b3b-d564-4ab6-8556-0148ebe7c108"|                                                                     |	"Activity Level Report for Payment"                                                                    |
  |"http://smartlifehealth.info/smh#954f0227-4142-4356-a372-0a7844931a4a"||	"EMW01 -report"                      |
  |"http://smartlifehealth.info/smh#5e324a9c-7e01-45f2-8b11-1d45ef859cdd"||	"Activity Level Report for Payment"                                                           |
  |"http://smartlifehealth.info/smh#ddc8ffa8-8f1f-4dab-b07a-0043f74f9912"|                                    |	"EMW02 -report"                            |
  |"http://smartlifehealth.info/smh#8547cf0c-84d6-400e-9bd6-2e678870341f"|                                                  |	"W01-ES-PAYMENT-Number of Consultation Utilised for Wound Care"|
  |"http://smartlifehealth.info/smh#190a4236-4c7e-415d-bd08-1fb223939e49"|         |	"W02-ES-PAYMENT- Number of Home Visits for Wound Care"|
  |"http://smartlifehealth.info/smh#9aae60a6-2f77-4aac-ac73-379e9ce41403"|       |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#00026fdc-8467-4573-a679-49cef833ddc4"|                        |	"EMW01 -report"                    |
  |"http://smartlifehealth.info/smh#af79fef6-0b33-4ae7-88b2-9a64d846cc7c"|                                  |	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#74dc7699-bd9a-4709-82a5-c0aeae1a88ff"|                 |	"EMW02 -report"       |
  |"http://smartlifehealth.info/smh#6606b6ea-839b-4ad0-bfc4-f791bda0233f"|                   |	"W01-ES-PAYMENT-Number of Consultation Utilised for Wound Care"|
  |"http://smartlifehealth.info/smh#974fa6a3-cec6-4d3c-8206-ddff945da02b"||	"W02-ES-PAYMENT- Number of Home Visits for Wound Care"                                                           |
  |"http://smartlifehealth.info/smh#050f6e67-b797-48e0-8fd4-038f2cfebe6c"||	"Activity Level Report for Payment"                                                         |
  |"http://smartlifehealth.info/smh#a3d52d66-454d-4020-b8fd-335350e5648f"||	"EMW01 -report"                                                             |
  |"http://smartlifehealth.info/smh#0045ee72-5a17-44f6-8a2d-1e52d0c878d7"|                           |	"Activity Level Report for Payment"                                                          |
  |"http://smartlifehealth.info/smh#51dac355-fcef-4356-93da-4bfe4a395250"|                |	"EMW02 -report"                                              |
  |"http://smartlifehealth.info/smh#6abf4e10-8db4-4611-9ee1-e99b89dea789"|                           |	"W01-ES-PAYMENT-Number of Consultation Utilised for Wound Care"|
  |"http://smartlifehealth.info/smh#6e1858e4-087a-4a7c-921b-35b920922d77"|       |	"W02-ES-PAYMENT- Number of Home Visits for Wound Care"                                   |
  |"http://smartlifehealth.info/smh#043ed309-9884-46c6-ab41-43586bb10b61"||	"Activity Level Report for Payment"                                       |
  |"http://smartlifehealth.info/smh#1bedf2ea-6aa7-4193-9e55-53cef69b690c"|                       |	"EMW01 -report"                             |
  |"http://smartlifehealth.info/smh#cda059ce-34c3-47f7-9b2c-be72fb155e63"|                                                            |	"Activity Level Report for Payment"                                 |
  |"http://smartlifehealth.info/smh#9d512bf4-4fc5-4a2b-b95d-01d1b52a368d"|                                |	"EMW02 -report"                    |
  |"http://smartlifehealth.info/smh#67731988-53cf-4f16-b456-f5bec34d9eb1"|                                       |	"CMD01a-ES-Patients consulted for COVID19 MISSING Enhanced Services Admin"|
  |"http://smartlifehealth.info/smh#e82afd48-b071-4135-8a8d-5f990e87203c"||	"CMD01b-ES-Patients consulted for COVID19 MISSING Treatment Codes"|
  |"http://smartlifehealth.info/smh#d479159b-5f02-462c-8056-64d0698b74df"||	"Anonymised Identifiers- DQ Report- MISSING Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#9a2d5243-5a39-45df-be42-5ad359bf3cba"||	"NHS Numbers- DQ Report- MISSING Enhanced Services Admin code"|
  |"http://smartlifehealth.info/smh#1c7e1a0d-730a-41ba-aff4-6af9de29338e"||	"Anonymised Identifiers- DQ Report- MISSING Treatment Codes"                                                     |
  |"http://smartlifehealth.info/smh#999612a0-6f24-4062-b707-9efca8e4f2e3"||	"NHS Numbers- DQ Report- MISSING Treatments Codes"   |
  |"http://smartlifehealth.info/smh#49c412d0-21eb-48f6-b63e-a0abc902777c"||	"CMD01-ES-Patients consulted for COVID-19 treatments"          |
  |"http://smartlifehealth.info/smh#7b992945-a903-465b-8f7b-f2e9b22fcc02"||	"Activity Level Report for Payment"|
  |"http://smartlifehealth.info/smh#e26e18dc-9c49-4975-9cff-f1cb2138e417"|               |	"EMCMD01 -report"|
  |"http://smartlifehealth.info/smh#53d99641-0528-41ac-981b-549a4312f4ca"|                                         |	"1. DL101-KPI-DEN-Patients on Diabetes QOF Register"|
  |"http://smartlifehealth.info/smh#6ed9e222-01b6-4db9-9131-5063f47adadc"||	"3. DL104-KPI-DEN-Diabetes QOF Register diagnosed in last 2 yrs & aged 17-70"                         |
  |"http://smartlifehealth.info/smh#51e89f90-9343-4805-b9c7-231e1d47ac72"|                                                                     |	"5. DL101-KPI-DEN-Patients on Diabetes QOF Register"                                                                |
  |"http://smartlifehealth.info/smh#908653d7-afdf-4ff0-ad71-ec7759472535"|                          |	"4. DL101-KPI-DEN-Patients on Diabetes QOF Register"|
  |"http://smartlifehealth.info/smh#c8c7a8af-4c2e-401c-a62d-20f28e0ee308"|                  |	"DM017 - Patients on Diabetes QOF Register"                                                           |
  |"http://smartlifehealth.info/smh#1e8dbda1-ddcf-48f0-b19a-2dfb4c6b1bd8"|                                                        |	"DM017 - Patients on Diabetes QOF Register (diagnosed in last 2 years)"|
  |"http://smartlifehealth.info/smh#6036bd99-1b91-49f3-9732-f1b824368808"|                          |	"DL101-KPI-DEN-Patients on Diabetes QOF Register"                                                    |
  |"http://smartlifehealth.info/smh#fd2974b2-2044-416f-a382-2b231c21b994"|   |	"DL102i-KPI-NUM-Patients with Smoking Status recorded (last 15m)"|
  |"http://smartlifehealth.info/smh#0ea4db69-a41a-4d75-9ffe-b53ecaab179e"|                                                     |	"DL102h-KPI-NUM-Patients with Right & Left Feet Risk Classifcation recorded"|
  |"http://smartlifehealth.info/smh#66108dbc-6a99-4dbb-b6e4-0387de62ba6f"||	"DL102g-KPI-NUM-Patients with Retinal Screening recorded (last 27m)"|
  |"http://smartlifehealth.info/smh#6482fa87-4327-4b30-8dfc-bc4986a66a8e"||	"DL102f-KPI-NUM-Patients with eGFR recorded (last 15m)"|
  |"http://smartlifehealth.info/smh#4f259370-6fc3-4602-81d2-c94dd3ca5112"||	"DL102e-KPI-NUM-Patients with Urine ACR recorded (last 15m)"|
  |"http://smartlifehealth.info/smh#94ff1ccc-f035-4d68-a54c-5329a9862fc7"|                                                                 |	"DL102d-KPI-NUM-Patients with Lipids recorded (in last 15m)"                           |
  |"http://smartlifehealth.info/smh#8699b40f-a4c7-4b7e-837e-69cb2248f32f"||	"DL102c-KPI-NUM-Patients with Blood Pressure recorded (in last 15m)"|
  |"http://smartlifehealth.info/smh#eb4fc256-6834-42cf-832c-2d4bd0ecd311"|                                                                 |	"DL102b-KPI-NUM-Patients with HbA1c recorded (in last 15m)"                                       |
  |"http://smartlifehealth.info/smh#c14f9c17-2afe-4cdd-8f73-20389b887030"||	"DL102a-KPI-NUM-Patients with BMI recorded (last 15m)"|
  |"http://smartlifehealth.info/smh#93b80269-5b54-4703-b31b-0735e9114c09"||	"*DL104N-KPI-NUM-Patients with latest HbA1c <= 48 (in last 15m)"|
  |"http://smartlifehealth.info/smh#25be145b-88b0-4116-9ae1-17ce51ecc3d3"|                                                           |	"DL108-KPI-NUM-Patients with a Care Plan (in last 15 months)"                                        |
  |"http://smartlifehealth.info/smh#bb74ea9e-fdaf-47e1-b558-cf48ecad1980"||	"*DL105-KPI-NUMERATOR-Patients with Mental Health Screening Completed" |
  |"http://smartlifehealth.info/smh#6134bdf0-6812-4613-a072-06a0b470bcda"||	"*DL102-KPI-NUM-Patients with 9 Key Care Process Completed"|
  |"http://smartlifehealth.info/smh#18289645-3ed7-4cb3-8f48-8afb173357c6"||	"1. SMI Eligible"                            |
  |"http://smartlifehealth.info/smh#77cdfe06-1c65-4e58-8440-3a126a07865a"||	"Asian, Chinese, other Asian, Middle Eastern, Black African"|
  |"http://smartlifehealth.info/smh#6a16a4f1-7cf9-4ffd-9345-94f1a199efd5"||	"01. Upload 1of1 PCN OBE 9KC v2.0.250508 -report"|
  |"http://smartlifehealth.info/smh#de495534-2968-49e7-a1f8-35bfe9417722"||	"Latest blood pressure reading excluding home/in home in the 12 months is null"|
  |"http://smartlifehealth.info/smh#ed5f9ae6-420c-4cc3-86e0-77ed0e100550"|                                                  |	"Latest blood pressure reading excluding home/in home in the 12 months"|
  |"http://smartlifehealth.info/smh#704aeb67-abe5-44f3-8705-d6f6f8da607f"||	"Latest blood pressure reading excluding home in the 12 months 150/90 rule 8 HYP009, CHD016,STIA015"|
  |"http://smartlifehealth.info/smh#7780dc3e-58f0-453a-832f-19c487377be8"|                                                          |	"Latest blood pressure reading done at home in the 12 months 145/85 rule 8 HYP009, CHD016,STIA015"                    |
  |"http://smartlifehealth.info/smh#a267b1c3-251c-405b-9e4a-4761053e50d3"||	"OB003 - Patients 18 or over on the obesity register"             |
  |"http://smartlifehealth.info/smh#50fe192d-e165-41ae-bc36-2688fc7ee593"||	"Adults with no BMI recorded L5Y"                                                         |
  |"http://smartlifehealth.info/smh#bc250388-cec5-4042-934e-b396ee13d17e"||	"Adults 18+"                                              |
  |"http://smartlifehealth.info/smh#16342673-cfea-45d8-bd61-aa9c0a856870"|                    |	"DH01c-Obesity-Patients with BMI > 27.5 or 30 depending on ethnicity"                                |
  |"http://smartlifehealth.info/smh#3d9d5652-0e22-4d4d-b049-aaa25f19c140"||	"OB003 - Patients 18 or over on the obesity register (2)"               |
  |"http://smartlifehealth.info/smh#5ab3f776-9072-497d-9e5f-8e7b3e2ad3b4"||	"NHS Cohort 1 >=4 Comorbidities BMI40+"                                    |
  |"http://smartlifehealth.info/smh#2bb5003a-9536-43f7-8d98-65c56d56985a"| |	"NHS Cohort 2 >=4 Comorbidities BMI35-40"  |
  |"http://smartlifehealth.info/smh#2d67afae-3d9a-4be4-8632-36b45263daf3"|           |	"Not on Mounjaro/Wegovy L6M or coded unsuitable"         |
  |"http://smartlifehealth.info/smh#466f68f7-b5f2-425a-b2dd-3340b904e979"|     |	"Currently on Mounjaro/Wegovy L6M"                                      |
  |"http://smartlifehealth.info/smh#5170f1a7-9702-42fc-b93b-e642a191c530"|                  |	"NHS Cohort 3 >=3 Comorbidities BMI40+"   |
  |"http://smartlifehealth.info/smh#97e3fff3-88ab-4777-b8a8-853168bff82e"|            |	"Not on Mounjaro/Wegovy L6M or coded unsuitable"|
  |"http://smartlifehealth.info/smh#5efa7adb-7c73-47ef-8b7d-47113b536632"|    |	"Currently on Mounjaro/Wegovy L6M"           |
  |"http://smartlifehealth.info/smh#753a615f-1cea-40fd-852f-ee67be6d97b0"|              |	"Not on Mounjaro/Wegovy L6M or coded unsuitable"|
  |"http://smartlifehealth.info/smh#c9deaf58-61fd-48d9-ab16-51f119c4c887"|   |	"Currently on Mounjaro/Wegovy L6M"                    |
  |"http://smartlifehealth.info/smh#cba19250-9365-4910-af34-729b6736a4f5"|                  |	"07. Upload 2of3 NWL MH CCMI v2.0.250615 -report"|
  |"http://smartlifehealth.info/smh#a608a64d-0bc2-4efc-b07c-91e553ec0cfd"|           |	"07. Upload 3of3 NWL MH CCMI v2.0.250615 -report"|
  |"http://smartlifehealth.info/smh#09f625a2-88d5-4978-b107-0608ddcbda70"|          |	"07. Upload 1of3 NWL MH CCMI v2.0.250615 -report"|
  |"http://smartlifehealth.info/smh#dd247574-e626-4fa2-8bf3-3f9bfbb2c795"|           |	"09. Upload 1of1 NWL NDH AR v2.0.250518 -report"|
  |"http://smartlifehealth.info/smh#fbd3f54e-dd0d-4f81-af6c-ac179126ff5b"||	"06. Upload 2of2 NWL MH SMI v2.0.250616 -report"|
  |"http://smartlifehealth.info/smh#18ce9597-8909-42a3-8bd7-87ee58fdff29"||	"06. Upload 1of2 NWL MH SMI v2.0.250616 -report"|
  |"http://smartlifehealth.info/smh#43ec9807-dde0-4ac4-8a4c-71a821fedca4"||	"14. Upload 1of1 PCN OBE 01 v2.0.250512 -report"        |
  |"http://smartlifehealth.info/smh#10479b5a-f260-480f-99dd-e6dbcdc0a5e9"||	"AS02Na-DQ-Patients MISSING Height recorded"                  |
  |"http://smartlifehealth.info/smh#e2230f12-d44f-4032-9fcd-8350e9b70816"||	"AS02Nb-DQ-Patients MISSING Weight recorded"|
  |"http://smartlifehealth.info/smh#58ba4258-4eb4-40f0-a923-f6182282a435"|                                                |	"AS02Nc-DQ-Patients MISSING BMI recorded"|
  |"http://smartlifehealth.info/smh#b96a03dd-f81a-4b7f-84dc-b6c84fb09c9d"||	"AS02Nd-DQ-Patients MISSING Blood pressure recorded"|
  |"http://smartlifehealth.info/smh#6a3d306b-4384-4369-9d2e-5fe4e462afee"||	"AS02Ne-DQ-Patients MISSING Pulse rate or pulse rhythm recorded"                                  |
  |"http://smartlifehealth.info/smh#355a09cb-11df-43af-a68b-44bc52e77491"|               |	"AS02Nf-DQ-Patients MISSING Smoking status recorded"|
  |"http://smartlifehealth.info/smh#75e9c848-2c94-48f7-b844-f767954446db"|                                             |	"*AS02N-DQ-Patients MISSING Health Assessment"             |
  |"http://smartlifehealth.info/smh#dfa27927-4445-411b-b42b-fd275b6b8c83"||	"ANONYMISED-DQ-Missing Health Assessment-Checklist -report"                                           |
  |"http://smartlifehealth.info/smh#be0e735c-cce7-469f-80af-fb945e8f8f63"|                                                           |	"ANONYMISED-DQ-Missing Health Assessment-More Detailed -report"                                                        |
  |"http://smartlifehealth.info/smh#d2061b06-824c-49ac-b103-852ae628be00"|             |	"NHS NUMBERS-DQ-Missing Health Assessment-Checklist -report"                              |
  |"http://smartlifehealth.info/smh#c13bd9b9-5d1c-4115-b2e4-5d68f832ac09"|            |	"NHS NUMBERS-DQ-Missing Health Assessment-More Detailed -report"|
  |"http://smartlifehealth.info/smh#92e3c15d-f047-49be-befb-e9806da1c2bf"||	"AS03N-DQ-Patients MISSING Medication Review"                                 |
  |"http://smartlifehealth.info/smh#95824e46-95da-4417-afbc-8e446cf46216"|                |	"ANONYMISED - DQ - Missing Medication Review -report"   |
  |"http://smartlifehealth.info/smh#8652ce51-de83-479d-91e9-6305d557ed43"|                        |	"NHS NUMBERS - DQ - Missing Medication Review -report"                                                  |
  |"http://smartlifehealth.info/smh#92469dfe-1c43-41c1-81b5-5c1d3a38a3be"|        |	"AS04N-DQ-Patients MISSING Flu Immunisation recorded" |
  |"http://smartlifehealth.info/smh#ecbb7d29-ac03-42ac-b50e-6b8f1cadac69"|     |	"ANONYMISED - DQ - Missing Flu Immunisation -report"                    |
  |"http://smartlifehealth.info/smh#7f14c864-5b1d-442f-8322-91490b8c9c2a"| |	"NHS NUMBERS - DQ - Missing Flu Immunisation -report"        |
  |"http://smartlifehealth.info/smh#5f91bd26-0e20-4cb8-b18f-5bfcc718ed48"| |	"AS05N-ES-Patients MISSING Safeguarding recorded"        |
  |"http://smartlifehealth.info/smh#50f0eb72-82aa-4c46-9540-f1219771a377"|                   |	"ANONYMISED - DQ - Missing Flu Immunisation -report"|
  |"http://smartlifehealth.info/smh#bd17ba01-6ad8-4a33-a429-80baa827b70a"||	"NHS NUMBERS - DQ - Missing Flu Immunisation -report"    |
  |"http://smartlifehealth.info/smh#65445888-a019-4b05-8fd8-a17eec0f336d"||	"AS06N-ES-Patients MISSING Mental Health Assessment recorded"             |
  |"http://smartlifehealth.info/smh#32b2461f-4961-4fe8-a1ae-7343ce0e519e"||	"ANONYMISED - DQ - Missing Mental Health Assessment -report"       |
  |"http://smartlifehealth.info/smh#b0b52c63-1d31-4696-88f0-b0bb34a339b5"||	"NHS NUMBERS - DQ - Missing Mental Health Assessment -report"                   |
  |"http://smartlifehealth.info/smh#7d797319-fe8c-4c0a-bb0a-564ff4756d59"||	"AS07N-ES-Patients MISSING Care Plan recorded"       |
  |"http://smartlifehealth.info/smh#805e577b-b75c-40be-b335-666334567324"||	"ANONYMISED - DQ - Missing Care Plan -report"        |
  |"http://smartlifehealth.info/smh#8d6e9d32-42f3-4ce0-9441-a4ab1fe26d3f"|                                |	"NHS NUMBERS - DQ - Missing Care Plan -report"|
  |"http://smartlifehealth.info/smh#08ebf448-f97d-4bab-b786-c3af163f370e"|                   |	"*CRM00  REGISTER  Patients on CRM Register"|
  |"http://smartlifehealth.info/smh#d519a0db-89d8-4a6c-8bd9-942dc63af861"|         |	"EMCRM00A -report"                                             |
  |"http://smartlifehealth.info/smh#b9f8930e-efc1-4286-be0a-4ee71bb56f47"|                                             |	"*RISK00A  Group 1  14 or more risk factors"|
  |"http://smartlifehealth.info/smh#7c7e4ff2-e60e-437f-a0a5-80fd1b7cddeb"|              |	"RISKA02 -report"|
  |"http://smartlifehealth.info/smh#e82fedde-35c0-41df-a1f2-439615956165"|                                                  |	"*RISK00B  Group 2  10-13 risk factors"|
  |"http://smartlifehealth.info/smh#cc9bfa9f-850b-4e4e-b943-d27472f83eda"|                    |	"RISKB02 -report"|
  |"http://smartlifehealth.info/smh#afc3bcd3-9f90-4cd4-9472-4857b0e24805"|                                             |	"*RISK00C  Group 3  0-9 risk factors"|
  |"http://smartlifehealth.info/smh#583f8cfe-9094-4432-88e0-c4b1aa1b100b"|                    |	"Female"         |
  |"http://smartlifehealth.info/smh#f8cc5e7d-c478-488d-be9c-90eb1dbbf24f"|                                                |	"Male"        |
  |"http://smartlifehealth.info/smh#0cf40712-391a-4da5-9125-48fe8a2ebae8"|                                                             |	"RISKC02a -report"|
  |"http://smartlifehealth.info/smh#b361fdcd-5f25-4ee3-bc61-c823ad59fe0b"|                                                      |	"RISKC02b -report"                            |
  |"http://smartlifehealth.info/smh#53e394b1-c93b-4cf7-847d-af6a66aaf98c"|                                                |	"CRM00B  LAST 15 MONTHS  First & Follow Up appointments"|
  |"http://smartlifehealth.info/smh#5210b67f-80ed-4101-907f-011138d9defc"|        |	"EMCRM00B -report"                   |
  |"http://smartlifehealth.info/smh#68ee7d8b-2cd8-44db-b4e1-d5743c6043de"|                                                     |	"CRM01DA  HYP  DQ  2 BP>=140/90 OR DYTM Avg BP>=135/85  MISSING Hypertension"|
  |"http://smartlifehealth.info/smh#ddf3d90d-c95b-4b24-bd3f-24536f03123d"||	"CRM01CA  DM  DQ  HbA1c >= 48 more than once  MISSING Diabetes Diagnosis"|
  |"http://smartlifehealth.info/smh#a73c9b12-937f-4bf3-af13-1ebb8d16ab4d"|                                                                 |	"CRM01CB  DM  DQ  2nd HbA1c btwn 42 & 47  MISSING NDH Diagnosis"|
  |"http://smartlifehealth.info/smh#0db45218-0858-4d4e-9f44-a34807d9e7d7"||	"NHS NUMBERS  EMCRM01D  Missing Hypertension Diagnosis -report"                                                               |
  |"http://smartlifehealth.info/smh#1f772e8f-9264-40f4-ab59-2d5b857c364e"||	"CRM01EA  NDH  DQ  2nd HbA1c >= 48 after HbA1c btwn 42 & 47  MISSING Diabetes"|
  |"http://smartlifehealth.info/smh#1e91cb0b-6eb7-47bc-9bc9-d8c0dddb2a43"||	"CRM01EB  NDH  DQ  More than 1 HbA1c btwn 42 & 47  MISSING NDH"|
  |"http://smartlifehealth.info/smh#a21004b8-2e35-49a3-bed1-91df13d3c18e"||	"NHS NUMBERS  EMCRM01C  Missing Diabetes Diagnosis -report"|
  |"http://smartlifehealth.info/smh#193485f7-4bb2-4470-b244-e7f86179800f"||	"NHS NUMBERS  EMCRM01C  Missing NDH Diagnosis -report"|
  |"http://smartlifehealth.info/smh#cb2a4436-6abf-4481-81c8-fedee9993f9f"|                     |	"CRM01CC  DM  DQ  HbA1c >= 48  MISSING 2nd HbA1c in FY"|
  |"http://smartlifehealth.info/smh#8c07cbba-f62c-4eea-804c-cdc15f6a8dfa"|      |	"CRM01DB  HYP  DQ  Earliest BP >= 140/90  MISSING 2nd Blood Pressure"|
  |"http://smartlifehealth.info/smh#7dab9bb6-5eaa-4920-a803-3b5da9cf17e2"|        |	"CRM01A  AF  DQ  MISSING ECG or Pulse Rhythm Check"                  |
  |"http://smartlifehealth.info/smh#bd692e90-dfa0-4a05-925e-23eac64701c4"|                      |	"CRM01B  CKD  DQ  MISSING CKD Diagnosis Code"|
  |"http://smartlifehealth.info/smh#f4b11b2c-61d1-498a-8211-87b7b90a8bea"|                  |	"NHS NUMBERS  EMCRM01E  Missing Diabetes Diagnosis -report"|
  |"http://smartlifehealth.info/smh#e5d2bf0c-4266-43e6-84d8-00ec27a3ac62"||	"NHS NUMBERS  EMCRM01E  Missing NDH Diagnosis -report"             |
  |"http://smartlifehealth.info/smh#c0cab413-3968-4da5-82f3-7e10d8a49ac0"||	"CRM01EC  NDH  DQ  HbA1c btwn 42 & 47  MISSING 2nd HbA1c in FY"                                              |
  |"http://smartlifehealth.info/smh#71ebe90c-cf54-499b-a020-4b4fc51bc6a8"||	"NHS NUMBERS  EMCRM01C  Missing HbA1c in Financial Year -report"|
  |"http://smartlifehealth.info/smh#3ba904e4-8425-4626-b2eb-72ff3a97b806"|        |	"NHS NUMBERS  EMCRM01D  Missing Blood pressure reading in Financial Year -report"|
  |"http://smartlifehealth.info/smh#07fd6ede-2461-4baf-8479-54c4baa0633f"||	"NHS NUMBERS  EMCRM01A  Missing ECG or Pulse Rhythm Check -report"                   |
  |"http://smartlifehealth.info/smh#424695ae-41f0-4660-9c97-0fd83d70adbf"||	"NHS NUMBERS  EMCRM01B  Missing CKD Diagnosis -report"                     |
  |"http://smartlifehealth.info/smh#3b3f3573-9dc5-4cc1-813c-8aacd94c1c9e"||	"NHS NUMBERS  EMCRM01E  Missing HbA1c in Financial Year -report"                                        |
  |"http://smartlifehealth.info/smh#fd9556cd-0a2f-4625-b7ac-d2dbe3172ff9"||	"CRM02a  ALL CRM  DQ  LAST 15M TO END OF FY  WITHOUT HbA1c"|
  |"http://smartlifehealth.info/smh#8021f7e3-e4f0-44bc-a5fd-7804bcfb9d4c"||	"CRM02b  ALL CRM  DQ  LAST 15M TO END OF FY  WITHOUT Blood Pressure"|
  |"http://smartlifehealth.info/smh#7046d477-38cb-4fba-80eb-987715fca38d"||	"CRM02c  ALL CRM  DQ  LAST 15M TO END OF FY  WITHOUT Lipids"|
  |"http://smartlifehealth.info/smh#8d9eb5df-491d-42f2-b602-9cc07f5aa529"||	"CRM02d  ALL CRM  DQ  LAST 15M TO END OF FY  WITHOUT Urine ACR"|
  |"http://smartlifehealth.info/smh#33e1fc9f-fbad-47b5-b143-a5f289337dc4"||	"CRM02e  ALL CRM  DQ  LAST 15M TO END OF FY  WITHOUT eGFR"|
  |"http://smartlifehealth.info/smh#d5a57eff-dac0-4a78-9c2f-d44216f369a4"|                                                                  |	"CRM02f  ALL CRM  DQ  LAST 15M TO END OF FY  WITHOUT BMI"                                 |
  |"http://smartlifehealth.info/smh#e87767e3-38f8-4c66-bb57-cbd7573e3a21"|          |	"CRM02h  ALL CRM  DQ  LAST 15M TO END OF FY  WITHOUT Smoking Status"|
  |"http://smartlifehealth.info/smh#39e63710-29ab-4db0-ab17-c3c5a1382164"||	"CRM02g  ALL CRM  DQ  LAST 15M TO END OF FY  WITHOUT Waist Circumference"|
  |"http://smartlifehealth.info/smh#13e05b37-9135-4394-82e3-a036ee3c9792"||	"CRM02i  DIABETES  DQ  LAST 15M TO END OF FY  WITHOUT MH Screening"|
  |"http://smartlifehealth.info/smh#81fbbdc7-f749-432c-9c31-3c0a78006db1"||	"CRM02j  DIABETES  DQ  LAST 15M TO END OF FY  WITHOUT Foot Check"|
  |"http://smartlifehealth.info/smh#7ed7f7d6-6a5f-4b2e-9e19-ac0745cbee52"||	"CRM02k  DIABETES  DQ  LAST 27M TO END OF FY  WITHOUT Retinal Screening"|
  |"http://smartlifehealth.info/smh#70d3cbca-a166-4a73-896a-e6e20418bd69"|                                                         |	"CRM02l  DIABETES OR MASLD  DQ  LAST 39M TO END OF FY  WITHOUT FIB-4"|
  |"http://smartlifehealth.info/smh#5ac3242d-1d90-4df3-81de-c16a480477be"||	"*CRM02  ALL CRM  DQ  Key Care Processes NOT Completed"                                |
  |"http://smartlifehealth.info/smh#983c2a2c-98b7-4a5b-a0d9-011825ed2cbd"||	"Diabetic Patients"                  |
  |"http://smartlifehealth.info/smh#2f432fec-cfdd-4d4d-bac5-f50bfe9f35b8"| |	"Metabolic dysfunction-associated steototic disease patients"|
  |"http://smartlifehealth.info/smh#b5e56822-2931-415e-a3e4-0f93726c9441"||	"NO Diabetes or Metabolic dysfunction-associated steototic disease"                                             |
  |"http://smartlifehealth.info/smh#e0514d75-c86c-4acf-a55d-70d97255594b"||	"NHS NUMBERS  CRM02  Key Care Processes NOT Completed -report"|
  |"http://smartlifehealth.info/smh#ac51968c-7807-42cd-9775-01b2c49a8d6d"||	"NHS NUMBERS  CRM02  Key Care Processes NOT Completed (more detailed) -report"                                                  |
  |"http://smartlifehealth.info/smh#e1c8af0b-18f4-4bc0-abec-8a37e2eb04f9"|                                                             |	"NHS NUMBERS  CRM02  Key Care Processes NOT Completed -report"                    |
  |"http://smartlifehealth.info/smh#026f8120-6f0a-4a76-8f06-130f2b3bfee7"||	"NHS NUMBERS  CRM02  Key Care Processes NOT Completed (more detailed) -report"|
  |"http://smartlifehealth.info/smh#b03b316c-2ecc-4be3-b439-1cddbfd507eb"||	"NHS NUMBERS  CRM02  Key Care Processes NOT Completed -report"|
  |"http://smartlifehealth.info/smh#a70f1152-8c9a-4069-a542-4f99de6e2d53"||	"NHS NUMBERS  CRM02  Key Care Processes NOT Completed (more detailed) -report"|
  |"http://smartlifehealth.info/smh#5906a578-65b0-4de1-9707-808e9e48d8ae"||	"CRM03A  DQ  NOT FRAIL OR AGE<79  LAST 15M TO END OF FY  Latest BP>130/80"|
  |"http://smartlifehealth.info/smh#00b8f860-ad2b-48f3-b6a1-898ebe25e67c"|                                              |	"CRM03B  DQ  FRAIL OR AGE>=80  LAST 15M TO END OF FY  BP>150/90"|
  |"http://smartlifehealth.info/smh#a3e27ec1-357f-4ae4-99f2-a0df907d3676"||	"CRM03  NHS NUMBERS  Blood Pressure > 130/80 -report"|
  |"http://smartlifehealth.info/smh#386f2a89-eec9-49db-874e-638fd4cc293a"||	"CRM03  NHS NUMBERS  Blood Pressure > 150/90 -report"|
  |"http://smartlifehealth.info/smh#77cd08a1-b5b3-4dd7-b062-35d616c035de"|                                                                 |	"CRM04  DQ  LAST 6M  NOT Prescribed Moderate or High Intensity Statin"|
  |"http://smartlifehealth.info/smh#e74d6fe1-3f0d-4ce0-ae4b-aae74b0eb442"||	"NHS NUMBERS  DQ  Moderate or High Intensity Statins NOT Prescribed -report"                                                             |
  |"http://smartlifehealth.info/smh#8e0aca18-0c00-4bc2-8fc3-b7345cdfe250"|                          |	"CRM05  DQ  LAST 6M  NOT Prescribed ACE inhibitor/Angiotensin Receptor Blocker"|
  |"http://smartlifehealth.info/smh#5aa14936-3313-460e-81e7-ead7cfdf8278"||	"NHS NUMBERS  DQ  ACE Inhibitor/ARB NOT Prescribed -report"                                                       |
  |"http://smartlifehealth.info/smh#8cd18e64-cac0-4bbd-975d-83bf8e01fbff"||	"CRM06  DQ LAST 6M  NOT Prescribed SGLT-2 inhibitors"                                                  |
  |"http://smartlifehealth.info/smh#f778ffc1-5ecf-49a3-a480-f32f50b1032f"||	"NHS NUMBERS  DQ  SGLT-2 inhibitors NOT Prescribed -report"|
  |"http://smartlifehealth.info/smh#7c556430-bb71-43db-9c32-c1fc439f67cb"||	"CRM07a  DQ  LAST 15M TO END OF FY  MISSING Care Plan"   |
  |"http://smartlifehealth.info/smh#923553c5-e542-4345-bc73-b1e3887c7532"||	"CRM07b  DQ  LAST 15M TO END OF FY  MISSING Eat"|
  |"http://smartlifehealth.info/smh#0662011a-94f6-45f1-be5d-5919eec74707"||	"CRM07c  DQ  LAST 15M TO END OF FY  MISSING Physical Activity"|
  |"http://smartlifehealth.info/smh#95550b12-ea67-473c-9f4c-eb13353202bd"||	"CRM07d  DQ  LAST 15M TO END OF FY  MISSING Sleep Pattern"|
  |"http://smartlifehealth.info/smh#b264ac7c-b614-4543-ba1d-ac775043be30"||	"CRM07e  DQ  LAST 15M TO END OF FY  MISSING Relax"   |
  |"http://smartlifehealth.info/smh#ef3f58ee-e1c8-43b3-b0e0-a222c2161583"||	"CRM07f  DQ  LAST 15M TO END OF FY  MISSING Connect"                          |
  |"http://smartlifehealth.info/smh#c9091b78-a38b-4738-95fb-31eb0b10a365"||	"CRM07  DQ  LAST 15M TO END OF FY  Holistic Care Plan NOT Completed"   |
  |"http://smartlifehealth.info/smh#0061349e-42d1-4220-858b-5c84d4d0fd44"|                     |	"CRM07g  DQ  LAST 15M TO END OF FY  MISSING Avoid harmful substances"                                                                 |
  |"http://smartlifehealth.info/smh#bd10c3a0-c0ab-4e78-bb33-88e771995169"|                                    |	"NHS NUMBERS  CRM07  Holistic Care Plan NOT Completed -report"               |
  |"http://smartlifehealth.info/smh#a299854c-6ee3-4c94-b0bc-e96e8fab2d23"||	"NHS NUMBERS  CRM07  Holistic Care Plan NOT Completed (more detailed) -report"                                                                      |
  |"http://smartlifehealth.info/smh#2337be18-f4de-4418-b58f-b3b8210e00e8"|                                             |	"CRM08Aa  DQ  MISSING 2nd Exercise Status codes"       |
  |"http://smartlifehealth.info/smh#51eb27a3-3335-4cf6-a06b-8ba3440d3511"|                                                      |	"CRM08Ba  DQ  MISSING 2nd BMI"|
  |"http://smartlifehealth.info/smh#747d4cb2-1b8f-4c74-b2dc-354ace38218d"||	"CRM08Ca  DQ  MISSING 2nd Smoking Status code"                  |
  |"http://smartlifehealth.info/smh#4c9d6610-8389-44fc-ad65-8f2469ab039e"|                                                                      |	"CRM08Ab  DQ  NO Improvement in Moderately Active or Active"                                             |
  |"http://smartlifehealth.info/smh#bcf6976e-8e50-4e9a-b072-991057ded87f"||	"NHS NUMBERS  CRM08A  MISSING 2nd Exercise -report"|
  |"http://smartlifehealth.info/smh#dedc64f7-b37b-420c-8ca8-b290dac2daaa"|                                                    |	"CRM08Bb  DQ  NO Improvement in BMI"                                         |
  |"http://smartlifehealth.info/smh#a3c7bb05-c1ca-43f3-a2b9-99b42c204810"|                |	"NHS NUMBERS  CRM08B  MISSING 2nd BMI -report"|
  |"http://smartlifehealth.info/smh#804257df-1b9f-4f65-89b6-b1eeda04fe21"|           |	"CRM08Cb  DQ  NO Improvement in Smoking Status"  |
  |"http://smartlifehealth.info/smh#9b303575-a6e5-4b77-b073-684a36f165bb"|          |	"NHS NUMBERS  CRM08C  MISSING 2nd Smoking Status -report"|
  |"http://smartlifehealth.info/smh#272d3fe6-2212-4867-b813-e8986c4eea9a"|            |	"NHS NUMBERS  CRM08A  No Improvement in Exercise -report"                                                       |
  |"http://smartlifehealth.info/smh#6f28a062-4dbf-43ab-a75a-d9ea5ca6cf29"|    |	"NHS NUMBERS  CRM08B  No Improvement in BMI -report"|
  |"http://smartlifehealth.info/smh#da4cbcf1-011e-4ae7-8a6a-ba326f960b7b"|   |	"NHS NUMBERS  CRM08C  No Improvement in Smoking Status -report"|
  |"http://smartlifehealth.info/smh#e4861eba-c862-4641-bc62-2d7583509eb4"|               |	"CRM09a  DQ  LAST 15M TO END OF FY  MISSING Health Confidence Score"|
  |"http://smartlifehealth.info/smh#0f5dab86-375d-4de2-bc8e-e9e5c246c653"||	"CRM09b  DQ  LAST 15M TO END OF FY  MISSING 2 Health Confidence Scores"|
  |"http://smartlifehealth.info/smh#1f17445d-8f33-4c82-a594-0f77d353f8cf"||	"NHS NUMBERS  CRM09  2 Health Confidence Scores -report"|
  |"http://smartlifehealth.info/smh#b9c585b8-198e-4515-b753-73f8ab066ea5"||	"NHS NUMBERS  CRM09  Health Confidence Score -report"|
  |"http://smartlifehealth.info/smh#6cfae607-eece-430e-bd71-c348b14940b0"||	"AF001  AF register"                |
  |"http://smartlifehealth.info/smh#8234245c-b080-4331-8a5c-d994fc4d5402"||	"AF001  BEFORE 1ST JAN NEXT YEAR  AF register"                                          |
  |"http://smartlifehealth.info/smh#087bcb2c-67a6-42d1-b881-b7d0190abf09"||	"CHD001  BEFORE 1ST JAN NEXT YEAR  CHD register"    |
  |"http://smartlifehealth.info/smh#02ca239d-f70c-4d32-969a-e920bf162b0e"|      |	"CHD001  CHD register"                        |
  |"http://smartlifehealth.info/smh#b91d5465-368f-4b7e-8346-9195ab569d47"|                                     |	"CKD005  BEFORE 1ST JAN NEXT YEAR  CKD register"|
  |"http://smartlifehealth.info/smh#d4ed5f10-d6a7-49ea-a62f-0fbbb87e8417"|         |	"CKD005  CKD register"|
  |"http://smartlifehealth.info/smh#ed7145cb-5411-4923-8210-e2b37caf3e0d"|                                                    |	"DM017  BEFORE 1ST JAN NEXT YEAR  Diabetes Register"|
  |"http://smartlifehealth.info/smh#08c4dfa3-a028-4fc6-9dfc-8dd78d642c84"|      |	"DM017  Diabetes Register"|
  |"http://smartlifehealth.info/smh#8bb2a085-5f10-48fd-9bd5-748575041e86"|                |	"HF1  BEFORE 1ST JAN NEXT YEAR  Unresolved diagnosis of heart failure"|
  |"http://smartlifehealth.info/smh#7259a275-cdee-4b25-a70e-2091d014f0ca"||	"HF1  Unresolved diagnosis of heart failure"|
  |"http://smartlifehealth.info/smh#4c6b8739-392f-4921-89fe-0b383660b9d8"|  |	"HYP001  BEFORE 1ST JAN NEXT YEAR  Hypertension register"|
  |"http://smartlifehealth.info/smh#fe3e3756-bef3-4ce1-acce-25c8540047d3"||	"HYP001  Hypertension register"           |
  |"http://smartlifehealth.info/smh#59e6e126-c62f-4646-b502-5e80f9f196aa"|               |	"MDST01  Metabolic dysfunction-associated steatotic disease"|
  |"http://smartlifehealth.info/smh#ce43430f-53a5-4cc7-8247-e8555ec9eb54"|               |	"MDST01BEFORE 1ST JAN NEXT YRMetabolic dysfunction-associated steatotic disease"                                                                    |
  |"http://smartlifehealth.info/smh#5b6647d9-af32-47d8-a248-f03aba1e62e0"||	"PAD001  BEFORE 1ST JAN NEXT YEAR  Peripheral arterial disease register"|
  |"http://smartlifehealth.info/smh#3b126896-15e3-41d4-8219-e45724ec00cc"||	"PAD001  Peripheral arterial disease register"|
  |"http://smartlifehealth.info/smh#68cd7392-9d6b-4e1d-8abe-de158ffb9621"||	"PC001  Palliative care register"        |
  |"http://smartlifehealth.info/smh#33f05d01-cad2-4621-898c-123d90c48e26"|                           |	"STIA001  BEFORE 1ST JAN NEXT YEAR  Stroke or TIA register"|
  |"http://smartlifehealth.info/smh#f6ef0c03-e271-482c-9b8b-7ee842dcd430"|              |	"STIA001  Stroke or TIA register"|
  |"http://smartlifehealth.info/smh#5c8594b2-1511-49a1-b426-cdc67f078435"|                                      |	"AF001 -report"                  |
  |"http://smartlifehealth.info/smh#6457a36c-35b6-475f-af29-7835451c6766"|                                          |	"CHD001 -report"|
  |"http://smartlifehealth.info/smh#ab2fafc1-4eba-472f-80d2-ff03860efe97"|                                                       |	"CKD005 -report"|
  |"http://smartlifehealth.info/smh#ccbc333a-a586-47d2-adfd-f37e523423c4"|                                                     |	"DM017 -report"     |
  |"http://smartlifehealth.info/smh#cfd807db-4a96-43c0-88b1-1c072ee21a55"|                                                    |	"NDH01  BEFORE 1ST JAN NEXT YEAR  Non-Diabetic Hyperglycaemia"|
  |"http://smartlifehealth.info/smh#5508065b-9554-4913-923a-c4c208d3bfa6"|   |	"NDH01  Non-Diabetic Hyperglycaemia"|
  |"http://smartlifehealth.info/smh#870e935d-33c6-4d60-b25f-dcbde72b8b79"|              |	"HF1 -report"                   |
  |"http://smartlifehealth.info/smh#f3a824db-939d-43e2-906d-1723a645a6f1"|                         |	"HYP001 -report"|
  |"http://smartlifehealth.info/smh#cc63bebb-a4bd-4b49-ac8d-c6c6ef8a94a4"|                     |	"MDST01 -report"|
  |"http://smartlifehealth.info/smh#b1576b3c-0705-4825-84e9-c34382b4ac48"|                     |	"PAD001 -report" |
  |"http://smartlifehealth.info/smh#3f91f0f5-fc13-432b-ac84-87abe62fd388"|                      |	"PC001a  Exclude Palliative Care Patients" |
  |"http://smartlifehealth.info/smh#a7c487e3-2f64-49cc-a2ce-2464f75a05d2"|                             |	"STIA001 -report"                                                         |
  |"http://smartlifehealth.info/smh#4ddaaee3-469e-4f51-bb96-d014cc97b57d"|                                                       |	"CRM00  BEFORE 1ST JAN NEXT YEAR  Patients on CRM Register"                                                                      |
  |"http://smartlifehealth.info/smh#0f776a3b-2047-4a42-b641-a867b5a88c9b"|                |	"NDH01 -report"                                             |
  |"http://smartlifehealth.info/smh#92959967-2151-431f-9224-5f1aad4408d9"|                                                       |	"CRM01Aa  AF  BEFORE START OF FY  AF register or PAF"|
  |"http://smartlifehealth.info/smh#4b63eaea-90ea-49a4-bca1-a3400daab6a4"|                      |	"CRM01Baa  CKD  BEFORE START OF FY  Patients with CKD 1-2"|
  |"http://smartlifehealth.info/smh#fefbb7b6-61cf-42c3-a010-739be490b979"|      |	"CRM01Bab  CKD  BEFORE START OF FY  Patients with CKD 3-5"|
  |"http://smartlifehealth.info/smh#941dbb17-2a1e-407c-9802-032dcb49ac05"|             |	"CRM01Bba  CKD  Latest eGFR<60 & 2nd eGFR <60 between 3m and 2yrs ago"|
  |"http://smartlifehealth.info/smh#83d85654-5024-40db-ae4f-3b41add3a4ee"||	"CRM01Bbb  CKD  uACR> 3 & 2nd uACR>3 between 1 wk & 2 yrs ago"|
  |"http://smartlifehealth.info/smh#37f4e615-3b01-4df5-818c-7f51ed151b04"||	"CRM01Bca  CKD  BEFORE 1ST JAN NEXT YEAR  First eGFR<60"|
  |"http://smartlifehealth.info/smh#ec4ae7ef-b116-4e6c-8600-7810899c2569"|          |	"CRM01Bcb  CKD  BEFORE 1ST JAN NEXT YR 1st uACR>3 OR Urine Protein/Creatine>30"|
  |"http://smartlifehealth.info/smh#2b3d8837-6d97-4807-8446-399966b9c249"||	"CRM01Caa  DM  BEFORE START OF FY  Diabetes Register"|
  |"http://smartlifehealth.info/smh#6860df74-ff79-42c2-867d-8edd525a1fcf"||	"CRM01Cab  DM  BEFORE 1ST JAN NEXT YEAR  HbA1c >= 48" |
  |"http://smartlifehealth.info/smh#a9138f6d-b4c5-455c-8aab-7080dd54f4bc"|            |	"CRM01Cba  DM  THIS FINANCIAL YEAR  Diabetes Register" |
  |"http://smartlifehealth.info/smh#ff292b75-27b1-4629-8770-b592f158a3c3"||	"CRM01Cbb  DM  HbA1c >= 48 on more than 1 ocassion"                  |
  |"http://smartlifehealth.info/smh#e71ce556-38ed-48db-8295-91cb3ecada80"||	"CRM01Cca  DM  THIS FINANCIAL YEAR  NDH diagnosis"               |
  |"http://smartlifehealth.info/smh#ed9560c6-51ff-4ea5-8216-d215b7ae8c25"||	"CRM01Ccb  DM  2nd HbA1c >= 42 & <48 after HbA1c >= 48"       |
  |"http://smartlifehealth.info/smh#c53cb3bd-bb14-49ed-a48e-797a32078bc6"||	"CRM01Cd  DM  DQ  THIS FY  1st HbA1c >= 48 and later HbA1c"           |
  |"http://smartlifehealth.info/smh#cb230dbf-8b8a-4fc6-bd38-2f819d6f7b8d"|      |	"CRM01Cd  DM  THIS FY  1st HbA1c >= 48 and later HbA1c < 42"                                          |
  |"http://smartlifehealth.info/smh#3118b83d-a54b-4f68-8f4f-55995da2db11"|         |	"CRM01Daa  HYP  BEFORE START OF FY  Hypertension Register"                             |
  |"http://smartlifehealth.info/smh#0795ffe5-8432-43ff-afd0-27126c50eb45"|  |	"CRM01Dab  HYP  BEFORE 1ST JAN NEXT YEAR  BP>=140/90 OR Daytime BP>=135/85"|
  |"http://smartlifehealth.info/smh#ca67aeb9-f5c8-43be-a11a-062d1d012c80"||	"CRM01Dbb  HYP  THIS FINANCIAL YEAR  Hypertension Register"                       |
  |"http://smartlifehealth.info/smh#0e91f7dc-56b8-4337-9e09-508d0ede08c3"||	"CRM01Dda  HYP  Daytime Average BP>=135/85"                             |
  |"http://smartlifehealth.info/smh#abf5e9a2-e777-473c-af89-fb1a18a9d639"||	"CRM01Eab  NDH  BEFORE 1ST JAN NEXT YEAR  HbA1c >= 42 & < 48"|
  |"http://smartlifehealth.info/smh#48e9f202-c34b-4268-a580-6e9b4f309c94"||	"CRM01Eba  NDH  HbA1c >=42 & <48 on more than 1 ocassion"|
  |"http://smartlifehealth.info/smh#68c90780-2b96-4aee-963c-c19914a5cdf5"||	"CRM01Eca  NDH  2nd HbA1c >=48 after HbA1c>=42 & <48"|
  |"http://smartlifehealth.info/smh#81898f68-acbf-486e-8ea6-241a2e01ba71"||	"CRM01Ed  NDH  2nd HbA1c < 42 after HbA1c>=42 & <48"                   |
  |"http://smartlifehealth.info/smh#22b8469e-4a5c-4694-a7a9-7d5fef4ce010"||	"CRM02ba  ACHIEVED  LAST 15M TO END OF FY  Not Home  Blood Pressure reading"                   |
  |"http://smartlifehealth.info/smh#b4655be7-4db1-41f7-81ad-6c881383b0bc"||	"CRM02ba  LAST 15 MONTHS  Not Home  Blood Pressure reading"           |
  |"http://smartlifehealth.info/smh#b7ee798b-68d5-4e7b-8716-6ea366db8ace"||	"CRM02bb  ACHIEVED  LAST 15M TO END OF FY  Home  Blood Pressure reading"                       |
  |"http://smartlifehealth.info/smh#636ed09f-0b1b-49be-961f-fee486ed6811"||	"CRM02bb  LAST 15 MONTHS  Home  Blood Pressure reading"                   |
  |"http://smartlifehealth.info/smh#ebd5f8f3-e636-47da-8b56-341f46f00b40"||	"CRM03aa  DQ  Not Frail  Not Home  LAST 15M TO END OF FY  130/80 BP reading"|
  |"http://smartlifehealth.info/smh#d0f59137-a2c8-4868-99fa-36760b8d1182"||	"CRM03aa  Not Home  Not Frail  IN LAST 15M  130/80 Blood Pressure reading"|
  |"http://smartlifehealth.info/smh#98bf266f-aa01-497c-8e6f-1b7c2006ff38"||	"CRM03ab  DQ  Not Frail  Home  LAST 15M TO END OF FY  130/80 BP reading"|
  |"http://smartlifehealth.info/smh#b665dad9-4585-4f26-b4e1-3a5e66af4443"||	"CRM03ab  Home  Not Frail  IN LAST 15M  130/80 BP reading"|
  |"http://smartlifehealth.info/smh#00ece5b0-4664-42dc-b7e5-88b122fe5bef"||	"CRM03ba  DQ  Frail  Not Home  LAST 15M TO END OF FY  150/90 BP reading"                                                                   |
  |"http://smartlifehealth.info/smh#aa552261-81d2-4620-b531-1a20bd2ef9a8"|                                                          |	"CRM03ba  Not Home  Frail  IN LAST 15M  150/90 Blood Pressure reading"|
  |"http://smartlifehealth.info/smh#6e263408-c6f3-4814-ac4a-125292fc1f4f"||	"CRM03bb  DQ  Frail  Home  LAST 15M TO END OF FY  150/90 BP reading"|
  |"http://smartlifehealth.info/smh#c9857c49-779d-48c7-99b6-53a7fd86bbfc"||	"CRM03bb  Home  Frail  IN LAST 15M  150/90 Blood Pressure reading"|
  |"http://smartlifehealth.info/smh#42e402fd-4e3a-47d4-a7f2-a7e1fec21d06"|                                                     |	"CRM03ca  Moderate or Severe Frailty or aged >= 80"|
  |"http://smartlifehealth.info/smh#4c0c41bb-32dc-4f80-a111-1aeb36f5dfac"|           |	"CRM05DBa  Urine ACR >= 3 or eGFR < 60"     |
  |"http://smartlifehealth.info/smh#9b8a4709-5761-4429-a508-9b154503491c"|                    |	"CRM08Ba  Earliest BMI >= 15 and < 30"|
  |"http://smartlifehealth.info/smh#22cb53ee-006c-44c4-84d7-ab1d1347636b"|                                       |	"CRM08Bb  Earliest BMI >= 30 and < 50"|
  |"http://smartlifehealth.info/smh#9f81c0ae-fe63-449a-9419-fc79ad728eb1"|                              |	"CRM08Bc  Earliest BMI >= 50 and <= 80"|
  |"http://smartlifehealth.info/smh#56facaed-6d3a-4848-b118-bd422adf1606"|                   |	"CRM10baa  Frail  DQ  LAST 15M TO END OF FY  BP reading exc home 150/90"|
  |"http://smartlifehealth.info/smh#f7cd54c9-81d8-4541-96fe-668b2830ba3e"|  |	"CRM10baa  Frail  LAST 15M  Blood Pressure reading excluding home 150/90"|
  |"http://smartlifehealth.info/smh#0f387211-bcd4-43a1-804f-071cb5dc762b"||	"CRM10bab  Frail  DQ  LAST 15M TO END OF FY  BP reading done at Home 150/90"                                                      |
  |"http://smartlifehealth.info/smh#12119fc4-4273-4590-af95-18aedb214b03"||	"CRM10bab  Frail  LAST 15M  Blood Pressure reading done at Home 150/90"      |
  |"http://smartlifehealth.info/smh#f6a9cbad-b271-4ae1-b971-282e6c507119"||	"CRM10bba  Not Frail  DQ  LAST 15M TO END OF FY  BP reading exc home 140/80"|
  |"http://smartlifehealth.info/smh#43ef633c-d92d-4287-b2aa-6124b47e870c"||	"CRM10bba  Not Frail  LAST 15M  BP reading excluding home 140/80"|
  |"http://smartlifehealth.info/smh#0098dfaa-41ea-41e6-86eb-8f5882a0e492"||	"CRM10bbb  Not Frail  DQ  LAST 15M TO END OF FY  BP done at Home 140/80"|
  |"http://smartlifehealth.info/smh#a41154b9-70e3-4471-b13e-286b80e433ea"||	"CRM10bbb  Not Frail  LAST 15M  BP reading done at Home 140/80"|
  |"http://smartlifehealth.info/smh#dd3e1b51-515b-4bb9-adcd-332bd2f37e34"||	"CRM12Na  DQ  THIS FY  Blood Pressure reading excluding home 140/90"|
  |"http://smartlifehealth.info/smh#20644598-78e1-4e33-a1ff-0a320fb02dfb"||	"CRM12Na  LAST 12M  Blood Pressure reading excluding home done 140/90"|
  |"http://smartlifehealth.info/smh#50e180c0-5f51-4bd3-ad6e-7cfed04d101b"||	"CRM12Nb  DQ  THIS FY  Blood Pressure reading done at Home 135/85"|
  |"http://smartlifehealth.info/smh#ac46be61-409a-4b01-b9a8-44f9a4abf425"||	"CRM12Nb  LAST 12M  Blood Pressure reading done at Home 135/85"|
  |"http://smartlifehealth.info/smh#2cab4db4-baa0-4562-928b-1cae46d09389"||	"DM017 - Patients on Diabetes QOF Register (diagnosed in last 2 years)"|
  |"http://smartlifehealth.info/smh#e1535257-9679-44e1-8229-c86da77c0ab1"|                                                                      |	"CRM04DA  CKD, CVD, DM or HF Registers"|
  |"http://smartlifehealth.info/smh#e5ff14f9-e799-44cd-bb46-26ed7d09b46c"|                             |	"CRM01Ba  CKD  BEFORE START OF FY  Patients with CKD 1-2 or CKD 3-5"|
  |"http://smartlifehealth.info/smh#d4f69db2-df48-4617-91ca-db7ac883b7b2"||	"CRM01Bb  CKD  2* eGFR<60 or 2*uACR>3"     |
  |"http://smartlifehealth.info/smh#2a077484-999f-49fb-be8c-2ab249f9dd47"|                                                          |	"CRM01Bc  CKD  BEFORE 1ST JAN NEXT YEAR  First eGFR<60 OR uACR>3"                                                                |
  |"http://smartlifehealth.info/smh#8e4212c0-e8ff-4c88-b238-38776fd52b23"|   |	"CRM01Eaa  NDH  BEFORE START OF FY  Diabetes or NDH or Gestational DM"|
  |"http://smartlifehealth.info/smh#c8e89932-8365-492a-afd8-e386159978a2"|                                                                     |	"CRM01Ca  DM  HbA1c >=48 before 1st Jan nxt yr AND NOT on DM Register"                                                                |
  |"http://smartlifehealth.info/smh#24e9ce1b-a814-423a-b02e-f687ac6027f5"||	"CRM01Cb  DM  Diabetes Diagnosis & HbA1c >=48 more than once"|
  |"http://smartlifehealth.info/smh#db87f9a5-e3c9-448b-9a19-52c78a4a8424"|                                                                    |	"CRM01Cc  DM  NDH Diagnosis and 2nd HbA1c btwn 42 & 47"                 |
  |"http://smartlifehealth.info/smh#f5d849d8-9447-4077-9b7a-bb3801ee53fb"|                                                      |	"CRM01Dba  HYP  More than once BP>=140/90"                        |
  |"http://smartlifehealth.info/smh#1bd1328e-5bad-4d46-893d-b616ccb22c40"|                       |	"CRM01Dca  HYP  BP<140/90 after prev BP>=140/90"|
  |"http://smartlifehealth.info/smh#8422e198-0c16-45e7-bd28-d9a741d3faff"|                                |	"CRM01Dcb  HYP  Daytime Average BP<135/85 after prev BP>=140/90"                                                          |
  |"http://smartlifehealth.info/smh#dd0b2987-b891-4f94-a108-031142fbce0d"|     |	"CRM01Dd  HYP  Daytime Average BP>=135/85 AND Hypertension Diag in FY"                   |
  |"http://smartlifehealth.info/smh#85d16546-535e-475d-90f8-0febb45f4bc8"||	"CRM01Eb  NDH  NDH Diagnosis & HbA1c >=42 & <48 more than once"|
  |"http://smartlifehealth.info/smh#dd64a769-bd01-4e78-8d67-fed1a36f4c73"||	"CRM01Ec  NDH  DM Diagnosis and 2nd HbA1c >= 48"|
  |"http://smartlifehealth.info/smh#3834cea0-5ab6-4f64-8833-cf44917c2548"||	"CRM03cb  No Moderate or Severe Fraility or aged < 80"                               |
  |"http://smartlifehealth.info/smh#f27db5a1-5a17-4700-be81-0003fb27fe6e"||	"CRM10ba  FRAIL  DQ  LAST 15M TO END OF FY  Latest BP<=150/90"|
  |"http://smartlifehealth.info/smh#df9a59c5-316c-47af-802c-585d97e3517a"||	"CRM10ba  FRAIL  ACHIEVE  LAST 15M  Latest BP<=150/90"                       |
  |"http://smartlifehealth.info/smh#b988f313-6ff1-4135-8de7-f490dcc560e2"||	"CRM10bb  NOT FRAIL  DQ  LAST 15M TO END OF FY  Latest BP<=140/80"   |
  |"http://smartlifehealth.info/smh#5c0b2800-5f6e-427e-832f-f0fd476d3789"||	"CRM10bb  NOT FRAIL  ACHIEVE  LAST 15M  Latest BP<=140/80"   |
  |"http://smartlifehealth.info/smh#34128b91-947c-4160-a0df-cc4a07e69b19"||	"CRM04DB  Atrial Fibrillation, Hypertension, MASLD or NDH  QRISK > 10%"|
  |"http://smartlifehealth.info/smh#8cc949fb-7e57-4e70-ba10-a71b1b7272ea"||	"CRM01Ea  NDH  HbA1c =>42 &<48 before 1st Jan nxt yr AND NO DM or NDH diagnosis"|
  |"http://smartlifehealth.info/smh#06731ed3-3c01-46eb-80d9-812778e4de26"||	"CRM01Db  HYP  More than once BP >= 140/90 AND Hypertension Diagnosis in FY"|
  |"http://smartlifehealth.info/smh#8e23fb97-d1c5-4aee-8a85-c0d8741962cb"|                                                                     |	"CRM01Dc  HYP  BP<140/90 or Daytime Avg BP<135/85 after prev BP>=140/90"|
  |"http://smartlifehealth.info/smh#89cb80bb-bef7-4274-82a5-01489becd5bb"||	"CRM02Da  CRM with NO Diabetes"             |
  |"http://smartlifehealth.info/smh#981a30ec-ed3b-4f43-acea-692ea068ad11"|                       |	"CRM02Dba  CRM with Diabetes or MASLD"       |
  |"http://smartlifehealth.info/smh#1a17f377-2ed6-4589-aec1-3f69401a8e29"|                           |	"CRM02Dbb  CRM with NO Diabetes or MASLD"|
  |"http://smartlifehealth.info/smh#fdbdb78d-ea60-4160-acac-d774775311b6"|                       |	"DL203a-Patients MISSING Insulin Treatment Initiation Code"|
  |"http://smartlifehealth.info/smh#698e363c-69c0-4cad-916a-784ba69908b9"|   |	"DL201b-MDT MISSING Enhanced Service Admin Code"    |
  |"http://smartlifehealth.info/smh#5108753d-1605-4e96-a398-a3bc5f3c585a"|             |	"DL203b-Insulin Initiation MISSING Enhanced Service Admin Code"|
  |"http://smartlifehealth.info/smh#6ac6c6c2-f9a4-4094-9a0e-47360f051f54"||	"DL204-GLP-1 Initiation MISSING Enhanced Service Admin Code"|
  |"http://smartlifehealth.info/smh#4089afb5-da0c-4ac3-88d9-2a91cfff0126"||	"DL205-Insulin Optimisation or intensification MISSING Enhanced Service Admin"|
  |"http://smartlifehealth.info/smh#5140b73c-4bd7-4719-9d2f-f5b03ed8672d"||	"Anonymised Identifers - DQ Report - MISSING Insulin Initiation Code"|
  |"http://smartlifehealth.info/smh#3c2b27dd-c5f2-4163-bcac-3ee98898e627"||	"NHS Numbers - DQ Report - MISSING Insulin Initiation Code"|
  |"http://smartlifehealth.info/smh#cc5137f4-914a-4fd1-b342-bcb21eb7e169"||	"Anonymised Identifiers - DQ Report - MISSING Enhanced Services Admin Code"|
  |"http://smartlifehealth.info/smh#e0cdafa0-44e0-4602-a88a-da092013c219"||	"NHS Numbers - DQ Report - MISSING Enhanced Services Admin Code"|
  |"http://smartlifehealth.info/smh#5c3279d5-90df-44bd-8d83-57a873793b0e"||	"Anonymised Identifiers - DQ Report - MISSING Enhanced Services Admin Code"|
  |"http://smartlifehealth.info/smh#0993c195-13fa-4d11-9b9e-e3fe7d3fbfe9"||	"NHS Numbers - DQ Report - MISSING Enhanced Services Admin Code"|
  |"http://smartlifehealth.info/smh#985bc3a9-5fb0-40b7-b86b-b348b2595b92"||	"Anonymised Identifiers - DQ Report - MISSING Enhanced Services Admin Code"|
  |"http://smartlifehealth.info/smh#2592bef1-2df2-4d9a-8aef-4a5f05a9688c"||	"NHS Numbers - DQ Report - MISSING Enhanced Services Admin Code"|
  |"http://smartlifehealth.info/smh#ebc6d14a-16ab-477b-b0ea-7578dd20cd6e"||	"Anonymised Identifiers - DQ Report - MISSING Enhanced Services Admin Code"                                                                     |
  |"http://smartlifehealth.info/smh#f4f8157e-3a86-4831-aacb-2a813ca73303"||	"NHS Numbers - DQ Report - MISSING Enhanced Services Admin Code"  |
  |"http://smartlifehealth.info/smh#1ca1a3bf-29f4-43d0-b7f7-36ab703fd7f3"|                                     |	"DL203-ES-Patients who may benefit from Insulin Treatment Initiation"|
  |"http://smartlifehealth.info/smh#d4952fda-acb6-4bf7-821b-d9f016c93eb4"|  |	"Anonymised Identifers - Patients who could benefit from Insulin Treatment -report"                   |
  |"http://smartlifehealth.info/smh#c64e268e-e408-4240-a0b5-d8a75fff44b1"||	"NHS Numbers - Patients who could benefit from Insulin Treatment -report"|
  |"http://smartlifehealth.info/smh#9a67221e-03c4-4df8-adb0-1cb0e7a04fac"||	"DL201a-Patients NOT discussed at MDT"                             |
  |"http://smartlifehealth.info/smh#9ef40cac-d458-4df8-b8c5-28d05ef69565"||	"Anonymised Identifers - DQ Report - MISSING MDT Code"                  |
  |"http://smartlifehealth.info/smh#4fdde8b2-941c-4b45-9413-8b64a6283808"|       |	"NHS Numbers - DQ Report - MISSING MDT Code"|
  |"http://smartlifehealth.info/smh#3a168456-a9b1-4e62-acce-1812bf755819"|                           |	"DL206f-ES-Patients referred to Weight Management Programmes (in FY)"|
  |"http://smartlifehealth.info/smh#83421a1f-7efc-4d68-a583-b6b04caa69b3"||	"DL206g-ES-Patients referred to ARRS Programmes (in FY)"|
  |"http://smartlifehealth.info/smh#038786a9-41c7-45fb-8ce0-b5f90635671b"|            |	"EMDL206f -report"|
  |"http://smartlifehealth.info/smh#eedf0849-58aa-4dd7-a80c-23f46a7a8745"|                      |	"EMDL206g -report"|
  |"http://smartlifehealth.info/smh#83e2705a-df02-490d-9627-53aee36af992"|                                                    |	"DL208f-ES-Patients referred to Weight Management Programmes (in FY)"|
  |"http://smartlifehealth.info/smh#3c57e03f-72f7-41ae-8f73-7180fd8086b9"||	"DL208g-ES-Patients referred to ARRS Programmes"                                      |
  |"http://smartlifehealth.info/smh#d9042312-9e68-4e2d-91f5-20415f9bec9a"|          |	"EMDL208f -report"                                            |
  |"http://smartlifehealth.info/smh#9c810041-782b-4c58-b66c-e45da04835ed"|                                                        |	"EMDL208g -report"|
  |"http://smartlifehealth.info/smh#addbd634-fcbd-44a2-855b-fd0783b5a41f"|                                  |	"DL209f-ES-Patients referred to Weight Management Programmes"|
  |"http://smartlifehealth.info/smh#b2692cbc-cd0f-452b-85de-50489cf0ef5b"|  |	"DL209g-ES-Patients referred to ARRS Programmes"|
  |"http://smartlifehealth.info/smh#6971c478-b6f5-4535-b2e5-b42afb0a0d06"|            |	"EMDL209f -report"         |
  |"http://smartlifehealth.info/smh#de01d3b5-de19-4e9c-bd14-eb5881b05492"|                                                  |	"EMDL209g -report"|
  |"http://smartlifehealth.info/smh#4fbe2b89-532b-40e8-8fe3-e122127f2ada"|                                             |	"DL202-Patients NOT seen under Early Onset Type 2 Diabetes Review"|
  |"http://smartlifehealth.info/smh#741079e1-580c-4489-9ff4-b92e2749cc17"||	"Anonymised Identifiers - DQ Report - MISSING Early Onset Type 2 Diabetes Review"|
  |"http://smartlifehealth.info/smh#c1857b36-f035-45b6-a839-084d08a0e4b0"||	"NHS Numbers - DQ Report - MISSING Early Onset Type 2 Diabetes Review"|
  |"http://smartlifehealth.info/smh#08e3b3c6-6f14-4352-9069-fa89a82168c6"||	"DL207f-Patients NOT Referred to Weight Management Programmes"|
  |"http://smartlifehealth.info/smh#ca14abd7-dcc3-48d9-8ee0-194ef384049a"||	"DL207g-Patients NOT Referred to ARRS Teams"|
  |"http://smartlifehealth.info/smh#57a948a6-8a4a-40d0-a034-bb29f3d2f744"||	"DL207h-Female Patients WITHOUT Preconception Advice recorded"   |
  |"http://smartlifehealth.info/smh#e72cb299-34b0-4394-8b74-adca01578d27"||	"NHS Numbers - DQ Report - MISSING Referral to Weight Mangement"|
  |"http://smartlifehealth.info/smh#1c61cb69-07de-4af9-9529-a3b3eb596c11"||	"NHS Numbers - DQ Report - MISSING Referral to ARRS Teams"    |
  |"http://smartlifehealth.info/smh#f7873ee6-f939-4b0c-90b5-c53db527e0f9"||	"DL207i-Female Patients WITHOUT Folic Acid Prescription"         |
  |"http://smartlifehealth.info/smh#7d96922b-8309-48b7-942d-961b9b2d3e9f"||	"NHS Numbers - DQ Report - MISSING Preconception Advice"        |
  |"http://smartlifehealth.info/smh#e5747cb1-a1fa-4d2f-a3fb-8acf96594d31"|          |	"NHS Numbers - DQ Report - MISSING Folic Acid Prescription"|
  |"http://smartlifehealth.info/smh#61dc8c1b-da66-4263-b23d-571c553c259f"|    |	"MH01-Patients with BMI recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ad824feb-8b46-44fd-8ca7-47c649570211"|             |	"MH02-Patients with Blood Pressure recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ec3b6fec-ebd0-4263-a4ab-b8c791fc3b07"|      |	"MH03-Patients with Diet Status recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ec773042-0e89-437d-8b11-c4737c892796"|         |	"MH04-Patients with Exercise Assessment recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#bcbc16fa-8757-42f9-a74f-229a4eba664b"||	"MH05-Patients with Smoking Status recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#959ccce6-a5eb-480b-aa33-8c7db7c6d532"||	"MH06-Patients with Alcohol Intake recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#95116c4b-9d2e-4eb4-bb05-6e35d1415d10"||	"MH07-Patients with Substance Misuse recorded (in Financial Year)"                                        |
  |"http://smartlifehealth.info/smh#8502f8c1-206e-4e02-bfbb-d2561e19e690"||	"MH08aD-Patients eligible for Cervical Cancer Screening recorded"|
  |"http://smartlifehealth.info/smh#053925ea-31bd-4167-84b8-ca68c98400f7"||	"MH08bD-Patients eligible for Breast Cancer Screening recorded"|
  |"http://smartlifehealth.info/smh#24f2b960-4450-4889-85a1-af6aea5f379e"||	"MH08cD-Patients eligible for Bowel Cancer Screening recorded"|
  |"http://smartlifehealth.info/smh#c296e827-5ee9-457a-ae76-fffbc0ce4d35"||	"MH08-Patients with appropriate Cancer Screening recorded"                                  |
  |"http://smartlifehealth.info/smh#73918597-05fd-4fc7-a156-44733d16b029"||	"MH09a-Patients with RaSWP recorded (in Financial Year)"                                             |
  |"http://smartlifehealth.info/smh#c28ab49d-1f36-4fe8-91f5-675adc75aeb2"||	"MH09b-Patients with Signs Unwell recorded (in Financial Year)"         |
  |"http://smartlifehealth.info/smh#6ef04c0b-2229-4d3d-b5b9-e79e57f5b234"||	"MH09c-Patients with Anticipatory Care Plan recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#44b59fa4-d3a2-4754-8547-d0650aa67de0"||	"MH09d-Patients with Health Action Plan recorded (in Financial Year)" |
  |"http://smartlifehealth.info/smh#79969dac-39a7-4eee-a729-fc85afd3c3a1"||	"MH09e-Patients with Patient Goals recorded (in Financial Year)"                                                             |
  |"http://smartlifehealth.info/smh#0f2ac1a2-4413-476b-a34b-d49560315624"||	"MH10-Patients with Medication Review recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#42a823c1-a7d9-4ad2-a0f7-979e4a247aa0"||	"MH11a-Patients with Serum Cholesterol AND on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#0b0bae52-83f0-466b-a264-51d360a1920d"|                                                                      |	"MH11b-Patients with Serum Cholesterol NOT on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#f245358a-2be3-493d-abb9-7489ca77ea0a"||	"MH11-Patients with Serum Cholesterol recorded"|
  |"http://smartlifehealth.info/smh#9750578d-e4bd-4cf8-8325-930d82c136a3"|                      |	"MH12a-Patients with HbA1c/Blood Glucose AND on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#7902c99a-5aed-4d1d-93be-78b8a61142a3"||	"MH12b-Patients with HbA1c/Blood Glucose NOT on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#3766f5e1-73ce-49ce-9467-3ade7ec7156c"||	"MH12-Patients with HbA1c/Blood Glucose recorded"|
  |"http://smartlifehealth.info/smh#be16147f-23b8-4abb-a5cb-763765b8ae31"||	"MH13D-Patients on Lithium"|
  |"http://smartlifehealth.info/smh#ced04184-8db6-49fa-b56e-46f32bd1a16e"|          |	"MH13-Patients with Lithium Monitoring recorded twice OR not on Lithium"|
  |"http://smartlifehealth.info/smh#ae316541-5695-4431-ade6-25ab2558bf1c"||	"MH14-Patients with Annual Review recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#d7529834-b8e3-4e2c-bb02-3ca5cfaf5b04"||	"MH15a-Total Follow Ups recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#41c799bd-f915-4447-9fd7-d72c3a67a174"||	"MH15b-Follow Ups recorded on same day as Annual Review (in FY) NOT PAYABLE"|
  |"http://smartlifehealth.info/smh#27cec914-0813-4707-8052-66f162a7d7f0"|                                                                      |	"MH15-Follow Ups not recorded on same day as Annual Review (in FY)"                        |
  |"http://smartlifehealth.info/smh#7482c8f7-4b56-4189-8009-f36a76cf7b0c"| |	"MH08aN-Patients advised about Cervical Cancer Screening advice (in FY)"|
  |"http://smartlifehealth.info/smh#7f7889c0-7afe-4795-8cc2-d4b783a4943f"||	"MH08bN-Patients advised about Breast Cancer Screening advice (in FY)"|
  |"http://smartlifehealth.info/smh#5f26abbd-0b55-47aa-b89b-edfddc5b3625"||	"MH08cN-Patients advised about Bowel Cancer Screening advice (in FY)"|
  |"http://smartlifehealth.info/smh#c1c114fe-734f-4eec-9750-a84aa3bf5d92"||	"MH00-ES-Patients with MH01-13 completed"                      |
  |"http://smartlifehealth.info/smh#1cdb552b-a5d0-49d8-af4b-cefabaa6f2c1"|           |	"MH13Na-Patients with Serum Lithium recorded twice (in Financial Year)"|
  |"http://smartlifehealth.info/smh#b4e065d9-117b-4091-913c-c5540248b095"||	"MH13Nb-Patients with eGFR recorded twice (in Financial Year)"                                 |
  |"http://smartlifehealth.info/smh#58a55f94-9354-4d21-ac02-b4ad39098326"||	"MH13Nc-Patients with Serum TSH recorded twice (in Financial Year)"|
  |"http://smartlifehealth.info/smh#6adab604-3b7f-46e3-a063-d82029527bbd"||	"MH13N-Patients with Lithium Monitoring recorded twice (in Financial Year)"|
  |"http://smartlifehealth.info/smh#48ac880c-f263-406b-b3c9-fc891cdfa7ca"|                                                         |	"MH01-Patients with BMI recorded (in Financial Year)"                                                 |
  |"http://smartlifehealth.info/smh#db88c220-94af-483e-a7f2-98ace2db7aba"||	"MH02-Patients with Blood Pressure recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ee17fe60-3e75-40d1-af98-1299fafcc4b5"||	"MH03-Patients with Diet Status recorded (in Financial Year)"        |
  |"http://smartlifehealth.info/smh#2c19de7d-a7a3-49bf-910d-2959a4553a8e"||	"MH04-Patients with Exercise Assessment recorded (in Financial Year)" |
  |"http://smartlifehealth.info/smh#a09e2e23-e14d-469d-b132-7b1f7af30a8f"||	"MH05-Patients with Smoking Status recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#3c5cd1c4-ebb2-4c7c-b416-21a45be5dd27"||	"MH06-Patients with Alcohol Intake recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#4cdf527b-e1b2-4254-949f-2e8fcc38c370"||	"MH07-Patients with Substance Misuse recorded (in Financial Year)"         |
  |"http://smartlifehealth.info/smh#9dd35a51-7fcb-4f14-9fba-9981bb0ca199"||	"MH08aD-Patients eligible for Cervical Cancer Screening recorded"     |
  |"http://smartlifehealth.info/smh#7def4c75-1ad8-493d-841a-81db737dc3d6"||	"MH08bD-Patients eligible for Breast Cancer Screening recorded" |
  |"http://smartlifehealth.info/smh#cce5cb30-6ab6-41b9-aa7a-c3b4fc6f8b94"||	"MH08cD-Patients eligible for Bowel Cancer Screening recorded"                   |
  |"http://smartlifehealth.info/smh#7dedca1f-b7b8-43f4-b4c8-83f1130088cc"||	"MH08-Patients with appropriate Cancer Screening recorded"|
  |"http://smartlifehealth.info/smh#cc09786f-d256-4f21-aedb-e3fa2fb379f4"||	"MH09a-Patients with RaSWP recorded (in Financial Year)"  |
  |"http://smartlifehealth.info/smh#d4e74470-55f3-47af-9af2-8d6eb18678c3"||	"MH09b-Patients with Signs Unwell recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#03506c65-13a4-46d4-a71f-a4d48734dbd1"| |	"MH09c-Patients with Anticipatory Care Plan recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#9ffcda84-486f-45c2-8889-af24ec43647f"||	"MH09d-Patients with Health Action Plan recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#c72cd0ee-3711-48d2-ae8c-a2c67e3be74e"|                                        |	"MH09e-Patients with Patient Goals recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#7fcba7e3-241e-421d-9dab-24f9524e3b89"|  |	"MH10-Patients with Medication Review recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#066b0043-2625-4933-bf7f-7ce9890a8214"||	"MH11a-Patients with Serum Cholesterol AND on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#e9f46cfd-b866-4ac6-989e-a0ef90009d17"||	"MH11b-Patients with Serum Cholesterol NOT on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#ac0b8f2e-0123-4834-ac6d-b229098f846c"|                                                                     |	"MH11-Patients with Serum Cholesterol recorded"|
  |"http://smartlifehealth.info/smh#948392d3-e231-4372-a95c-e3011891c32b"|                             |	"MH12a-Patients with HbA1c/Blood Glucose AND on Anti Psychotics recorded"                                                              |
  |"http://smartlifehealth.info/smh#3af5a61d-7e0f-4ec2-a322-47cc3cd9d0b3"||	"MH12b-Patients with HbA1c/Blood Glucose NOT on Anti Psychotics recorded"                        |
  |"http://smartlifehealth.info/smh#f9c02884-8ec2-4c20-aec0-e9368502ae12"||	"MH12-Patients with HbA1c/Blood Glucose recorded"  |
  |"http://smartlifehealth.info/smh#9bce6ca9-1b91-470b-9b73-c417043a3b54"|                                                                     |	"MH13D-Patients on Lithium"|
  |"http://smartlifehealth.info/smh#fe7927b6-394d-43d1-a365-0982630b4a41"|                                                      |	"MH13-Patients with Lithium Monitoring recorded twice OR not on Lithium"                                                 |
  |"http://smartlifehealth.info/smh#b427bc64-6c28-4e9f-92b7-0ea553564648"||	"MH14-Patients with Annual Review recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#a6c28b7d-90d0-458f-b0ce-f96b4d009a8d"||	"MH15a-Total Follow Ups recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ef9d3eec-8258-4c02-a3ee-7694d26ab77e"||	"MH15b-Follow Ups recorded on same day as Annual Review (in FY) NOT PAYABLE"           |
  |"http://smartlifehealth.info/smh#78ad7861-ce62-481b-af30-6b0f82409bf3"|                                             |	"MH15-Follow Ups not recorded on same day as Annual Review (in FY)"                                                    |
  |"http://smartlifehealth.info/smh#1b9cfbd3-ee86-41d8-bd2d-a7a0da52d630"||	"MH08aN-Patients advised about Cervical Cancer Screening advice (in FY)"  |
  |"http://smartlifehealth.info/smh#d65eb299-b103-4fd9-b1a5-1a451abd5955"||	"MH08bN-Patients advised about Breast Cancer Screening advice (in FY)"|
  |"http://smartlifehealth.info/smh#59fb94c9-6562-4a2f-8202-7b37ee91c42b"||	"MH08cN-Patients advised about Bowel Cancer Screening advice (in FY)"                   |
  |"http://smartlifehealth.info/smh#19248ff1-1229-42ed-a147-39fa4d11cec8"||	"MH00-ES-Patients with MH01-13 Completed"                            |
  |"http://smartlifehealth.info/smh#60707fbe-b188-4bb7-952a-2a261a994581"||	"MH13Na-Patients with Serum Lithium recorded twice (in Financial Year)"|
  |"http://smartlifehealth.info/smh#854ffbc9-dda7-47e5-a863-e9cf36cbd5f6"||	"MH13Nb-Patients with eGFR recorded twice (in Financial Year)"|
  |"http://smartlifehealth.info/smh#d8a05d63-2df9-4bbd-a7be-66041319ce81"||	"MH13Nc-Patients with Serum TSH recorded twice (in Financial Year)"                                               |
  |"http://smartlifehealth.info/smh#da6c4af9-76c3-4c0a-a23a-ef2bb8fe406e"||	"MH13N-Patients with Lithium Monitoring recorded twice (in Financial Year)"                                                                  |
  |"http://smartlifehealth.info/smh#e6805abe-7337-48ca-afe6-39f924fe928d"||	"SMI00-DQ-CHECK-SMI Patients in Remission"                                       |
  |"http://smartlifehealth.info/smh#854975d8-b6f8-4a8f-bd4e-6e1dfc42bd93"||	"SMI01-DQ-SMI Patients in Remission with Annual Review or Follow Up"     |
  |"http://smartlifehealth.info/smh#6d6f4a4b-0b6e-4090-8e3c-b1fc2e53f5d6"||	"MH00-DQ-First Appt or Follow Up WITHOUT CCMI or SMI Recorded"|
  |"http://smartlifehealth.info/smh#bf3a8c89-0411-4e87-aab6-40551850f918"||	"*SMI00-DQ-SMI Patients with Incomplete First Appt or Follow Up"                                 |
  |"http://smartlifehealth.info/smh#ce7ffbe7-4b36-40b0-87b1-e632680b6881"||	"*CC00-DQ-CCMI Patients with Incomplete First Appt or Follow Up"|
  |"http://smartlifehealth.info/smh#75dd23fb-8db7-4d61-9f40-cdbea3680ed4"|                                                |	"SMI Register - MDS Report - NHS Numbers"|
  |"http://smartlifehealth.info/smh#1a6ca9e0-d1fe-498e-9d46-b1a8e4bd9330"|      |	"CCMI Register - MDS Report - NHS Numbers"|
  |"http://smartlifehealth.info/smh#8d283d6c-4cdf-47e1-ba9c-c27a09352811"|                                   |	"RESP01Na-DQ-MISSING Optimise Treatment (in Financial Year)"|
  |"http://smartlifehealth.info/smh#90d9d476-b8eb-4bf2-8e8e-56195fe86871"|     |	"RESP01Nb-DQ-MISSING Pulmonary Rehab (in Financial Year)"|
  |"http://smartlifehealth.info/smh#42454e4a-9f1b-450e-88dd-6e4a96d981be"||	"RESP01Nc-DQ-MISSING Tobacco dependence services (in Financial Year)"                                                                    |
  |"http://smartlifehealth.info/smh#4decc59d-853a-471f-a37d-0dc1d5e72ba6"||	"RESP01Nd-DQ-MISSING Inhaler Technique (in Financial Year)"|
  |"http://smartlifehealth.info/smh#fca3be5a-be0d-4d18-91f4-d73a045f56d2"||	"RESP01Ne-DQ-MISSING Offered or Administered Vaccine (in Financial Year)"|
  |"http://smartlifehealth.info/smh#987f801d-cab4-46d9-afc5-297e9b012ec2"||	"RESP01Nf-DQ-MISSING Physical Activity (in Financial Year)"|
  |"http://smartlifehealth.info/smh#37586feb-058b-4eb4-9ffe-07c6d6f42507"||	"RESP01Ng-DQ-MISSING Support for Psychosocial Wellbeing (in Financial Year)"|
  |"http://smartlifehealth.info/smh#c88540dd-712d-4f62-9c9f-a6eed8aac5ff"||	"RESP01Nh-DQ-MISSING Education and Self-management (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ea252b26-9e26-4619-947b-b991d0bce0cf"||	"*RESP01N-DQ-MISSING 8 Care Processes"|
  |"http://smartlifehealth.info/smh#ae8bfe0c-387b-48b6-b00a-d2b69b7c926e"|    |	"ANONYMISED-DQ-MISSING 8 Care Processes -report"|
  |"http://smartlifehealth.info/smh#8cfb1a45-4d11-4eb2-9aef-19d13f091883"|                                 |	"NHS NUMBERS-DQ-MISSING 8 Care Processes -report"|
  |"http://smartlifehealth.info/smh#0b6453ea-1d0e-405f-9400-cdbbf176524c"|                        |	"RESP02N-DQ-MISSING Inhaler Technique (in Financial Yr)"                                                                   |
  |"http://smartlifehealth.info/smh#d86bc2bf-0811-4f9d-bbd1-398a0a3fdb96"|                    |	"ANONYMISED-DQ-Missing Inhaler Technique -report"           |
  |"http://smartlifehealth.info/smh#5cfdd068-a10a-460e-82d8-c2a441585069"|                          |	"NHS NUMBERS-DQ-Missing Inhaler Technique -report"|
  |"http://smartlifehealth.info/smh#5a9e1343-1b32-4fb3-b2dd-e44e590642de"|       |	"RESP03-DQ-Asthma patients diagnosed in FY MISSING MART or AIR Inhaler Therapy"|
  |"http://smartlifehealth.info/smh#e74f2964-9720-4041-900d-64ea3c2b4213"||	"ANONYMISED-DQ-MISSING MART or AIR inhaled therapy -report"                                                   |
  |"http://smartlifehealth.info/smh#52c985d4-e042-486b-8d25-4585fcaf2cff"||	"NHS NUMBERS-DQ-MISSING MART or AIR inhaled therapy -report"                     |
  |"http://smartlifehealth.info/smh#fb1cac29-82e3-4535-ab82-6784ac4bff57"||	"CKD01D-ES-DENOMINATOR-Patients who are likely to have CKD"              |
  |"http://smartlifehealth.info/smh#ee8d8be3-22fb-4e29-8ac5-f6194867a174"||	"CKD01Dd-ES-Latest eGFR<60 & 2nd eGFR <60 between 3m and 2yrs ago"          |
  |"http://smartlifehealth.info/smh#db5c4577-4b3e-4f50-af11-bbe933b3cd33"||	"CKD01Db-ES-Patients with CKD 3-5 (before start of Financial Year)"        |
  |"http://smartlifehealth.info/smh#8ceaefa8-0ebe-4a96-a6d5-df6017eb6c53"||	"CKD01Da-ES-Patients with CKD 1-2 (before start of Financial Year)"  |
  |"http://smartlifehealth.info/smh#fe68cd0d-16c2-4d9e-9e32-fcc9826cc0df"|                                                            |	"CKD01Df-ES-Patients with 2* eGFR<60 or 2*uACR>3"|
  |"http://smartlifehealth.info/smh#4e8b46af-2b38-4b2f-b3ad-318fdfbf8315"|     |	"CKD01De-ES-Patients with uACR> 3 & 2nd uACR>3 between 1 wk & 2 yrs ago"|
  |"http://smartlifehealth.info/smh#98fe96df-723f-4952-b23c-6999d3038f95"||	"CKD01Dc-ES-Patients with CKD 1-2 or CKD 3-5 (before start of Financial Year)"|
  |"http://smartlifehealth.info/smh#eb3a7ddc-428d-46d4-9dbb-0d1484d5d765"||	"CKD01N-ES-NUMERATOR-Patients diagnosed and coded with CKD (in FY)"                                                                   |
  |"http://smartlifehealth.info/smh#e230caf6-e7c9-4eaa-bda8-04b8ab1682dd"|                                                                 |	"EMCKD01N -report"|
  |"http://smartlifehealth.info/smh#311d04b0-210b-4e1a-8b9a-b15ed5a07a97"|                                             |	"CKD02D-ES-DENOMINATOR-Patients on CKD 3a-5 Register"                               |
  |"http://smartlifehealth.info/smh#37ce03a7-0ed2-419c-8f22-c66e1598216d"|      |	"CKD005 - Patients on the CKD register"               |
  |"http://smartlifehealth.info/smh#51cbce12-a1ec-4ed0-ac48-872674134ec7"|               |	"CKD02N-ES-NUMERATOR-Patients with CKD Annual Review (in Fin Year)"|
  |"http://smartlifehealth.info/smh#d25f5553-a2bf-45ed-83cf-06e30b3fe596"||	"EMCKD02N -report"                  |
  |"http://smartlifehealth.info/smh#661042b9-0d8e-406a-9848-6b26b362bd30"|                     |	"HYP01D-ES-DENOMINATOR-Black & Black British patients with Hypertension aged < 80"|
  |"http://smartlifehealth.info/smh#58da0555-87ee-4cf0-8ce0-b4e94cd02a30"||	"HYP001 - Patients on the hypertension register"|
  |"http://smartlifehealth.info/smh#fbbe2f34-cd5e-434a-a313-585d36919638"| |	"HYP01N-ES-NUMERATOR-Latest BP <= 140/90 in last 12 months"|
  |"http://smartlifehealth.info/smh#b9b7e891-cf05-4cf4-af0e-0cbd0971e216"|                     |	"HYP01Na-Blood Pressure reading excluding home done in last 12 months 140/90"                                                            |
  |"http://smartlifehealth.info/smh#1c002cc1-74f5-49ff-ae2f-15aaad544c17"||	"HYP01Nb-Blood Pressure reading done at Home in last 12 months 135/85"|
  |"http://smartlifehealth.info/smh#9815ed2c-c65a-4bcc-b125-19b8cee375b0"||	"EMHYP01N -report"                  |
  |"http://smartlifehealth.info/smh#672c7eb9-8933-44a1-92da-3c62680043ff"|                  |	"HYP02D-DENOMINATOR-ES-Patients on Hypertension Register"|
  |"http://smartlifehealth.info/smh#3e618619-c12e-4876-95ce-5dcb70965270"|      |	"HYP001 - Patients on the hypertension register"                                     |
  |"http://smartlifehealth.info/smh#010fc92e-b50e-4e5d-8f25-08ba166581d6"|                        |	"HYP02N-ES-NUMERATOR-Patients with NO Blood Pressure recorded (in last 12 months)"|
  |"http://smartlifehealth.info/smh#52d0944d-088b-4ac0-a4bf-f1ac75297a0b"||	"HYP02Na-Blood Pressure reading excluding home done in last 12 months"|
  |"http://smartlifehealth.info/smh#79a14ccb-efdd-464c-ba8c-fb5e1e66451d"||	"HYP02Nb-Blood Pressure reading done at Home in last 12 months"|
  |"http://smartlifehealth.info/smh#7ed558f3-6e3b-47e2-a4dc-1e7854024765"||	"EMHYP02N -report"                                                             |
  |"http://smartlifehealth.info/smh#0dd9c583-c873-4b28-bbd9-b0671096a57d"|                                                             |	"CC00a-ES-Patients on Anti-Psychotics (in FY)"|
  |"http://smartlifehealth.info/smh#f8b1d24c-fbc7-448c-bc21-5bddaadcca8a"|                       |	"SMI Register - Patients with SMI (MH001)"                   |
  |"http://smartlifehealth.info/smh#a0496a4a-25dd-45a7-909d-55e1a3b9a53f"|              |	"MH001 - Patients on the mental health register"                   |
  |"http://smartlifehealth.info/smh#3f8144b7-73b1-4644-8c1f-012c890404b9"||	"Patients on Serious Mental Illness (SMI) Register (excluding those in remission)"|
  |"http://smartlifehealth.info/smh#19dc227c-4efe-4600-8839-6f8b5a3ec1fb"||	"MH1.1 - Psychosis, schizophrenia or bipolar diagnosis (pts in remission)"|
  |"http://smartlifehealth.info/smh#41b57871-a5a1-48ae-991c-359b916412fe"||	"CCMI - Patients who can be seen under MH service"         |
  |"http://smartlifehealth.info/smh#88730105-6131-49cb-b946-aad5d15bdb96"|                                                            |	"POTENTIAL patients for inclusion on CCMI register"|
  |"http://smartlifehealth.info/smh#9e6695f6-f334-41d4-bff3-f3b329d8a25d"||	"POTENTIAL patients for inclusion on CCMI register (2)"|
  |"http://smartlifehealth.info/smh#fbbc7341-c3ed-4b0f-a44a-4a7f35a61235"||	"DEP1_REG - Depression Register (to calculate CCMI target)"|
  |"http://smartlifehealth.info/smh#db158477-d6e0-4ca2-8bfa-d2bf8e759e33"||	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis"|
  |"http://smartlifehealth.info/smh#8d95d179-0604-42fe-8b3f-5ba5cc539b58"||	"MH2_REG - Lithium treatment with prescription in last 6 months"                                                                |
  |"http://smartlifehealth.info/smh#2ffff415-7e9a-4107-bd4a-ff8d3a39ca6b"||	"MH13b-ES-Patients with Lithium Monitoring recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#3e058b7e-983f-42ec-aabb-9d1465fb8da5"||	"MH13a-Lithium treatment with prescription in financial year"                         |
  |"http://smartlifehealth.info/smh#44a46851-ce25-4712-9a2c-57b5b61c66da"||	"MH13ba-ES-Patients with Serum Lithium recorded twice (in FY)"        |
  |"http://smartlifehealth.info/smh#ab23de67-2b1f-4c23-a10b-bf30c39e6a27"||	"MH13bb-ES-Patients with eGFR recorded twice (in FY)"                                                                      |
  |"http://smartlifehealth.info/smh#5916fd86-cab2-4eef-8ebb-aa4255b0bc5f"||	"MH13bc-ES-Patients with Serum TSH recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#6830b603-daad-4885-a661-bd8bac00dfdc"||	"SMI Register - Patients with SMI (MH001)"                   |
  |"http://smartlifehealth.info/smh#d0688977-dff0-4573-afcc-3f1eb7feb0cd"||	"MH001 - Patients on the mental health register"       |
  |"http://smartlifehealth.info/smh#cd204830-0fa5-4dac-a0db-8d59ae93b9e5"|               |	"MH08c-ES-Patients with Bowel Cancer Screening advise OR not eligible (in FY)"                        |
  |"http://smartlifehealth.info/smh#e944ad41-843a-4e14-87d4-120160ba5f91"||	"MH08cb-NUM-Patients advised about Bowel Cancer Screening (in FY)"  |
  |"http://smartlifehealth.info/smh#1289cc26-17b9-49cc-b234-70c0dfa53d94"||	"MH08ca-ES-Patients aged 60-74"                                     |
  |"http://smartlifehealth.info/smh#bed55f1f-d772-466a-9f6a-327018f0cbc5"|                |	"MH08b-ES-Patients with Breast Cancer Screening advise OR not eligible (in FY)"|
  |"http://smartlifehealth.info/smh#6febc1a7-33f0-4e4f-ab5b-f8632125f193"||	"MH08bb-ES-Patients advised about Breast Cancer Screening (in FY)"|
  |"http://smartlifehealth.info/smh#9060af46-86db-4504-8152-897364e6226f"||	"MH08ba-ES-Female patient aged 50-70"       |
  |"http://smartlifehealth.info/smh#70fba559-8600-4ad5-876a-b9a2c2c926f5"||	"MH08a-ES-Patients with Cervical Cancer Screening advise OR not eligible (in FY)"|
  |"http://smartlifehealth.info/smh#1f2e0430-a559-4590-8c46-b8a683724e2c"||	"MH08ab-ES-Patients advised about Cervical Cancer Screening (in FY)"|
  |"http://smartlifehealth.info/smh#ab49544e-1f3e-40e5-b7ca-fbcd7be70573"||	"MH08aa-MDS-Female patient aged 25-64 with no history of hysterectomy"|
  |"http://smartlifehealth.info/smh#6d717ddb-bc0d-4663-8d45-103cd11d516a"|                                                       |	"MH13-ES-Patients with Lithium monitoring recorded twice OR not on Lithium(in FY)"|
  |"http://smartlifehealth.info/smh#533ac64a-df79-4f42-9ff4-11803bfc7434"||	"Patients on Serious Mental Illness (SMI) Register (excluding those in remission)"                                                 |
  |"http://smartlifehealth.info/smh#e8deebaa-c447-4c87-ae77-18c4757bbf6d"||	"MH1.1 - Psychosis, schizophrenia or bipolar diagnosis (pts in remission)"|
  |"http://smartlifehealth.info/smh#eae628e9-521b-4ebf-bde3-07f53576a081"||	"MH08-ES-Patients with appropriate Cancer Screening Prompts recorded (in FY)"|
  |"http://smartlifehealth.info/smh#201b5b1f-c002-413c-8c48-ccd6d8e5c60e"||	"Patients on Complex Common Mental Health Illness (CCMI) Register (exc SMI)"|
  |"http://smartlifehealth.info/smh#a668945b-fbd7-4fd3-9583-66e0992e5f0b"|            |	"MH08-Patients with appropriate Cancer Screening recorded"                                                                     |
  |"http://smartlifehealth.info/smh#8b406da3-31f0-4c1e-976e-0da438ef6f94"|        |	"MH07-Patients with Substance Misuse recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#e78743f3-4d35-462c-84d4-6580aaf02c18"|    |	"MH07-ES-Patients with Substance Misuse recorded (in FY)"|
  |"http://smartlifehealth.info/smh#4fba0cfa-ced6-4c6b-9e7a-dc8e7e6fab3e"||	"MH04-Patients with Exercise Assessment recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#1318788a-2b26-4fc3-bf51-8b9b044ac884"||	"MH04-ES-Patients with Exercise Assessment recorded (in FY)"|
  |"http://smartlifehealth.info/smh#57745c29-0c5f-459e-a708-51af52cc2869"||	"MH03-Patients with Diet Status recorded (in Financial Year)"   |
  |"http://smartlifehealth.info/smh#52846623-247a-4109-a937-22c9cd0a5e90"||	"MH03-ES-Patients with Diet Status recorded (in FY)"                                                               |
  |"http://smartlifehealth.info/smh#77cf5476-e090-4fd1-8ad7-d5baf4b01db7"||	"MH00-ES-Patients with MH01-13 completed"                             |
  |"http://smartlifehealth.info/smh#2915281a-42b6-4cd6-a0c1-0b9d4ad49693"||	"MH01-ES-Patients with BMI recorded (in FY)"|
  |"http://smartlifehealth.info/smh#7575c8cd-3aa4-45f7-9cd0-263efaf5c40a"|      |	"MH02-ES-Patients with Blood pressure recorded (in FY)"                 |
  |"http://smartlifehealth.info/smh#d34d666e-836d-4e42-a755-0ce53602aab4"|          |	"MH05-ES-Patients with Smoking Status recorded (in FY)"|
  |"http://smartlifehealth.info/smh#357eff75-5394-4271-be7a-2cc0e975119c"||	"MH06-ES-Patients with Alcohol Intake recorded (in FY)"|
  |"http://smartlifehealth.info/smh#f8c51fb7-b73a-4c11-8c83-9f1027f1476c"||	"MH09a-ES-Patients with RaSWP recorded (in FY)"|
  |"http://smartlifehealth.info/smh#ff4c376a-9e60-498c-9868-73b31598a206"|                |	"MH09b-ES-Patients with Signs Unwell recorded (in FY)"|
  |"http://smartlifehealth.info/smh#e15ec2e9-9e0e-4ad2-8417-f357b7bc3efe"||	"MH09c-ES-Patients with Anticipatory Care Plan recorded (in FY)"|
  |"http://smartlifehealth.info/smh#95cfec93-655b-4f68-813a-1a33ff82b716"||	"MH09d-ES-Patients with Health Action Plan recorded (in FY)"|
  |"http://smartlifehealth.info/smh#a9bfcbd1-7e96-4ad1-8ca5-db4258e02e93"||	"MH09e-ES-Patients with Patient Goals recorded (in FY)"|
  |"http://smartlifehealth.info/smh#34419837-674b-44e4-aaf6-46008253cb19"||	"MH10-ES-Patients with Medication Review recorded (in FY)"|
  |"http://smartlifehealth.info/smh#41cf46b4-d7b7-41ba-a648-070f610e353a"||	"MH11-ES-Patients with Serum Cholesterol recorded"                  |
  |"http://smartlifehealth.info/smh#9b7c7d9d-b054-4ddf-aa3d-e68d866fb6a6"||	"MH12-ES-Patients with HbA1c/Blood Glucose recorded"|
  |"http://smartlifehealth.info/smh#c66d1db0-5627-4f0d-9866-b9f07cf8ed92"|                                   |	"*MH00-ES-PAYMENT-Patients with Payable Follow Up AND ALL required MDS Completed"                                                        |
  |"http://smartlifehealth.info/smh#c5008258-a313-4268-8d96-87fe4af1275e"||	"MH15b-ES-Patients with Annual Review and Follow Up not recorded on the same day"|
  |"http://smartlifehealth.info/smh#2df21a16-3d05-484d-95c3-f2818a3c5130"|                  |	"MH02a-Blood Pressure reading excluding home done in Financial Year"|
  |"http://smartlifehealth.info/smh#feefde86-4ff6-40b2-a343-e8015f2445dd"|   |	"MH02b-Blood Pressure reading done at Home in Financial Year"|
  |"http://smartlifehealth.info/smh#f0ab396f-39cf-4686-beca-9aa7ef17f4b5"|                                  |	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis"     |
  |"http://smartlifehealth.info/smh#537f3189-ca01-4fdb-8d2a-322099a1bd8d"|                  |	"MH2_REG - Lithium treatment with prescription in last 6 months"|
  |"http://smartlifehealth.info/smh#aad5340e-78e9-451f-990d-3cdc2cfc70d7"|                                      |	"MH11a-ES-Patients with Serum Cholesterol AND on Anti Psychotics (in FY)"|
  |"http://smartlifehealth.info/smh#f43253e6-7272-4c42-ad22-f3f7622f6de2"||	"MH11b-ES-Serum Cholesterol NOT on Anti Psychotics aged over 35 yrs(in last 3yrs)"                                         |
  |"http://smartlifehealth.info/smh#998e7a9d-c902-4cfa-b02f-7bb131a271e8"||	"MH11c-ES-Patients aged under 35 yrs NOT on Anti Psychotics"                                   |
  |"http://smartlifehealth.info/smh#816b199e-1351-4a1d-894f-85ebfa7de3af"||	"MH12a-ES-Patients with HbA1c/Blood Glucose AND on Anti-Psychotics (in FY)"                              |
  |"http://smartlifehealth.info/smh#644debd2-65da-4040-bc14-130c64196ded"|  |	"MH12b-ES-HbA1c/Blood Glucose NOT on Anti-Psychotics aged over 35 yrs(last 3yrs)"                   |
  |"http://smartlifehealth.info/smh#2301f148-42ce-4a80-be67-522f4486c10c"||	"MH12c-ES-Patients aged under 3yrs NOT on Anti-Psychotics (last 3yrs)"                                           |
  |"http://smartlifehealth.info/smh#a7b810a5-a94e-4369-99f4-19f468c52c76"||	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis"            |
  |"http://smartlifehealth.info/smh#1f64065b-4624-436a-b315-b114f30baeb8"||	"MH001 - Patients on the mental health register"                                                          |
  |"http://smartlifehealth.info/smh#9fe76005-71f9-46e0-92f6-c796757bb080"||	"MH2_REG - Lithium treatment with prescription in last 6 months"                           |
  |"http://smartlifehealth.info/smh#6820f071-f442-4406-88fd-0fef0177dac5"||	"SMI Register - MDS Report - NHS Numbers"                              |
  |"http://smartlifehealth.info/smh#d9cefc31-6a98-4ffb-a4d9-7c25c4b4340b"||	"Upload FULL NWL SMI 1.5-CP v2.0.250425 (Latest1) -report"                                                            |
  |"http://smartlifehealth.info/smh#98e7c399-e791-432a-98f5-10f4f0be1651"||	"Mental Health Dashboard -report"   |
  |"http://smartlifehealth.info/smh#1ecc7348-01c1-4bd5-89c6-fceca535e55f"|                                                                      |	"POTENTIAL patients for inclusion on CCMI register"|
  |"http://smartlifehealth.info/smh#a7341285-9df1-40f9-8ce9-7688269af833"|                                                     |	"Not SMI Register"|
  |"http://smartlifehealth.info/smh#bc75e8ae-896a-41f1-bb9b-ffe230a80946"|                                           |	"DEP1_REG - Patients aged 18 or over with unresolved depression since April 2006"                                           |
  |"http://smartlifehealth.info/smh#27ca1dc5-bc2e-4385-8fd4-b610d7418e43"||	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis"                                                     |
  |"http://smartlifehealth.info/smh#d3bc4576-1bc3-472b-aa33-d0dde6385010"||	"MH2_REG - Lithium treatment with prescription in last 6 months"                                                              |
  |"http://smartlifehealth.info/smh#8a6f8f5a-8149-404f-a3fc-56c3cab64731"||	"DH01-KPI-DEN-Patients with Non-Diabetic Hyperglycaemia"|
  |"http://smartlifehealth.info/smh#79d14f57-6272-4c47-9364-92744f940800"|                                                                     |	"DL101-KPI-DEN-Patients on Diabetes QOF Register"                   |
  |"http://smartlifehealth.info/smh#705d0e7c-b9db-49b3-8570-8fbc17c7a530"||	"DH01-KPI-DEN-Patients with Non-Diabetic Hyperglycaemia (aged 17-80)"   |
  |"http://smartlifehealth.info/smh#10c7f69e-6fb6-4909-833c-5ba6c15d6d47"|     |	"DH04g-KPI-NUM-Patients with Smoking Status recorded (last 15m)"|
  |"http://smartlifehealth.info/smh#b607915e-98e9-4a18-a45a-d58c02e0aa50"|             |	"DH04f-KPI-Patients with latest Cholesterol (in last 27m)"                                                        |
  |"http://smartlifehealth.info/smh#69f089c1-16b3-47f2-86b7-1dcff9c7276d"| |	"DH04e-KPI-NUM-Patients with Diet Lifestyle Advice recorded (last 15m)"|
  |"http://smartlifehealth.info/smh#09c1598c-d811-4edc-be52-c1d3150ae4df"||	"DH04d-KPI-NUM-Patients with latest Exercise recorded (last 15m)"|
  |"http://smartlifehealth.info/smh#b70a3c12-3893-467c-89d3-45b1dbb168ec"||	"DH04c-KPI-NUM-Patients with latest BMI recorded (last 15m)"   |
  |"http://smartlifehealth.info/smh#8ba65897-87cb-4621-9afa-6601039b8a1f"||	"DH04b-KPI-NUM-Patients with latest Blood Pressure (in last 15m)"                                               |
  |"http://smartlifehealth.info/smh#a9df4efc-df48-428e-8bb6-d6f797dcece8"||	"DH04ba-Blood Pressure reading excluding home done in last 15 months"|
  |"http://smartlifehealth.info/smh#2caab1f4-1d40-4bc2-9f0e-52cf9b801b43"|                                                  |	"DH04bb-Blood Pressure reading done at Home in last 15 months"|
  |"http://smartlifehealth.info/smh#c9a8d5de-76f4-4e66-bc1b-132a7bc15e2b"||	"DH04a-KPI-NUM-Patients with latest HbA1c (in last 15m)"|
  |"http://smartlifehealth.info/smh#a93554e5-c7eb-470d-be7f-eb1aba28b7b5"||	"*DH04-KPI-NUM-Patients with Annual Review Completed"|
  |"http://smartlifehealth.info/smh#e2b2d61f-6954-4316-974a-2f7895116c9c"||	"DM017 - Patients on Diabetes QOF Register"                                        |
  |"http://smartlifehealth.info/smh#52192eb8-490d-4a5b-bd25-91a941a31080"|            |	"DH01-KPI-DEN-Patients with Non-Diabetic Hyperglycaemia (aged 17-80)"                         |
  |"http://smartlifehealth.info/smh#99ca0cbe-f39a-4540-8761-815c3fad695f"||	"DH01-KPI-DEN-Patients with Non-Diabetic Hyperglycaemia"|
  |"http://smartlifehealth.info/smh#b137fb24-1875-42de-a736-034f36f1b949"||	"DL101-KPI-DEN-Patients on Diabetes QOF Register"                                          |
  |"http://smartlifehealth.info/smh#1e26feb1-ab76-4b88-bdc4-658a9dbe5ecc"||	"DH03-ES-KPI-NUM-Patients with NDPP started recorded (last 15m)"|
  |"http://smartlifehealth.info/smh#9f9ea86e-3464-4da3-9c64-38b58f72f085"||	"DM017 - Patients on Diabetes QOF Register"                 |
  |"http://smartlifehealth.info/smh#31565ea2-ca34-4f89-9344-a4c550c5dc9e"||	"DH02D-KPI-DEN-Patients diagnosed Non-Diabetic Hyperglycaemia in last 5 years"                                |
  |"http://smartlifehealth.info/smh#aaec8082-e81a-4429-848f-e48c079471dd"||	"DH02-KPI-DEN-Patients diagnosed Non-Diabetic Hyperglycaemia in last 5 years"|
  |"http://smartlifehealth.info/smh#558deab0-af4a-4ec0-9aa5-29af862d5a13"||	"DH02N-Patients diagnosed with Type 2 Diabetes in last 15 months"                                                                     |
  |"http://smartlifehealth.info/smh#16bbf4e0-1dd6-4d98-81ac-44adf219e2da"|                                                               |	"ASCVD"    |
  |"http://smartlifehealth.info/smh#b0f607e5-fe5c-48c0-ba24-dd0da43287d8"|                                                 |	"HF2 Register - Unresolved heart failure due to LVSD or reduced EF"|
  |"http://smartlifehealth.info/smh#49938113-672d-45b2-be4f-22a7eb748b1d"||	"HF1 Register - Patients with an unresolved diagnosis of heart failure"|
  |"http://smartlifehealth.info/smh#8afedebc-4506-4cce-8a20-25805067350f"||	"Patient has a received two invitations for cholesterol monitoring, made at least 7 days apart, and had no cholesterol recordings during the 12 months leading up to and including the achievement date."|
  |"http://smartlifehealth.info/smh#95b1fbf7-45f0-42de-926c-b970f577c710"||	"CHOL2REG - Patients with CHD / PAD / TIA"|
  |"http://smartlifehealth.info/smh#76d88962-9583-4549-8b9f-a95d53bb3e36"||	"Patient has a received two invitations for cholesterol monitoring, made at least 7 days apart, and had latest non-HDL cholesterol test where the value is 2.6 mmol/L or over."               |
  |"http://smartlifehealth.info/smh#ec22c9b1-3833-499b-b07f-3e4d4edab429"|                    |	"Patient has a latest cholesterol reading in the 12 months up to or on the payment period end date, was above target levels and had 2 invitations for cholesterol monitoring, made at least 7 days apart"|
  |"http://smartlifehealth.info/smh#ccdebfa9-077e-4a63-aadc-6cdccd596a2b"||	"[CHOL004] - Patients with LDL cholesterol <=2.0 or non-HDL <=2.6"|
  |"http://smartlifehealth.info/smh#a7d321f5-1d16-417e-82e1-96a21bb13629"|   |	"Patients Registered in the 9 month period"|
  |"http://smartlifehealth.info/smh#8f3daa17-564f-4383-a686-e2586cf20e77"|                      |	"CHOL004 - Patients with LDL cholesterol <=2.0 or non-HDL <=2.6"|
  |"http://smartlifehealth.info/smh#cb979021-c76f-42eb-b25d-8e9411a9188f"||	"Lipid lower therapy, LDL>=4.1, HDL<1m, HDL<1.3f, Triglyceride >=1.7"                     |
  |"http://smartlifehealth.info/smh#41005d9f-4878-4ba4-9fd4-8ea610f10d8b"||	"Patients issued with lipid lowering therapy L6m"             |
  |"http://smartlifehealth.info/smh#7e0e7217-e4fd-4bd8-9e01-e19ba4b96a97"|            |	"HYP001 - Patients on the hypertension register"                                       |
  |"http://smartlifehealth.info/smh#e1fa3066-847c-4d57-a13a-b341f568939d"||	"BPHOMEBPLAT_DAT is equal to HOMEBP_DAT"                         |
  |"http://smartlifehealth.info/smh#563a02d4-7fc0-4b18-b4c7-0fc6a46f8f19"||	"BPHOMEBPLAT_DAT is equal to BPEXHOME_DAT"                                      |
  |"http://smartlifehealth.info/smh#03729468-9c50-4a8f-a105-cca34098eecf"||	"BPHOMEBPLAT_DAT null or less than Payment period end date in last 12 months"|
  |"http://smartlifehealth.info/smh#bc5923fb-424c-4aba-a396-2a51e2a87f01"||	"Latest blood pressure reading excluding home in the 12 months for HYP009 rule 8"|
  |"http://smartlifehealth.info/smh#f4a24794-fb46-45f5-ae60-f1eb2e0ecae8"|                                 |	"Latest blood pressure reading excluding home in the 12 months for HYPINVITE1_DAT, HYPINVITE2_DAT"                                                       |
  |"http://smartlifehealth.info/smh#10b22958-bfb9-40b0-9dab-abe734bb3fd9"||	"Latest blood pressure reading excluding home in the 12 months for HYP008"|
  |"http://smartlifehealth.info/smh#1622910e-ed6f-4d22-bca2-cbdf4e7a223a"||	"Latest blood pressure reading excluding home in the 12 months 140/90 rule 8 DM033, HYP008, CHD015, STIA014"|
  |"http://smartlifehealth.info/smh#c1d1d403-a96a-4fc6-83ac-a046cd7ad1b2"|                                                                 |	"Latest blood pressure reading done at home in the 12 months for HYP009"|
  |"http://smartlifehealth.info/smh#3c2146c0-26ac-45f0-841b-84dd7a482edd"||	"Latest blood pressure reading done at home in the 12 months for HYPINVITE1_DAT, HYPINVITE2_DAT"|
  |"http://smartlifehealth.info/smh#38b7cdfd-97c6-413e-a3b3-197bda1fa011"|                              |	"Latest blood pressure reading done at home in the 12 months for for HYP008"|
  |"http://smartlifehealth.info/smh#e5d01b42-0d13-4b92-83bf-715287139850"||	"Latest blood pressure reading done at home in the 12 months for rule 8 DM033, HYP008, CHD015, STIA014"|
  |"http://smartlifehealth.info/smh#bbead444-ecff-4714-9193-3ea115b50e08"|                                                                  |	"Latest blood pressure reading BPHOMEBPLAT, HYP"                                          |
  |"http://smartlifehealth.info/smh#c3b4db80-2508-4ea5-b207-7b33882e1f82"|             |	"[HYP009] - Hypertension aged 80 or over eligible for BP"|
  |"http://smartlifehealth.info/smh#d3c8c62e-66ad-4cc3-928a-1bbda706c060"|        |	"Blood Pressure reading excluding home done in last 12 months HYP009, CHD016,STIA015 rule 2 150/90"                                  |
  |"http://smartlifehealth.info/smh#8d84175b-90f1-4a4f-a0ee-aea669a6727b"||	"Blood Pressure reading at home done in last 12 months HYP009, CHD016,STIA015 rule 3 145/85"|
  |"http://smartlifehealth.info/smh#d8e89a1c-3d48-4971-a099-88f84f11221e"||	"Patients are receiving maximal blood pressure therapy in the 12 months"|
  |"http://smartlifehealth.info/smh#b064d841-893b-4e5a-9272-ce4a6b65d628"|                                                  |	"Hypertension quality indicator care was unsuitable in the 12 months"|
  |"http://smartlifehealth.info/smh#2cd2b7e1-ff49-4e7e-9640-69230223dc4f"||	"BPDEC_DAT/HOMEBPDEC_DAT/ABPMDEC_DAT"|
  |"http://smartlifehealth.info/smh#875d86ce-35ba-4185-a0b1-dbdfb92f197d"||	"patients are not receive hypertension quality indicator care in the 12 months"|
  |"http://smartlifehealth.info/smh#5f3c2852-a05c-49ab-bad5-ea7b861da208"||	"Earliest Occurrence of Hypertension is within last 9 months"        |
  |"http://smartlifehealth.info/smh#748ea7a1-1000-4658-a110-8d82e1ea3571"|                                            |	"Patients Registered in the 9 month period"|
  |"http://smartlifehealth.info/smh#a14e460f-3363-450b-a0db-c01a0702d526"||	"[HYP008] - Hypertension aged 79 or under eligible for BP"|
  |"http://smartlifehealth.info/smh#a288a541-96a8-4aec-a542-464f6f4e4e8b"||	"Blood Pressure reading excluding home done in last 12 months 140/90 for rule 2 DM033, HYP008, CHD015, STIA014"                  |
  |"http://smartlifehealth.info/smh#5f0a106d-d588-44fe-8159-d360b05a3b67"|                      |	"Blood Pressure reading done at Home in last 12 months for rule 3 DM033, HYP008, CHD015, STIA014, 135/85"|
  |"http://smartlifehealth.info/smh#6be1c633-e886-4b11-bb0e-275597b7764f"|                                                                      |	"HYP009 - Hypertension aged 80 or over BP 150/90 mmHg or less"   |
  |"http://smartlifehealth.info/smh#c1e03d66-1333-4027-b9d5-bc68a05fccca"||	"HYP008 - Hypertension aged 79 or under BP 140/90 mmHg or less"|
  |"http://smartlifehealth.info/smh#91dc8171-4931-4d1a-a41e-dcc8a7ce2026"||	"Patients on Hypertension Register who don't have a resolved code parent"|
  |"http://smartlifehealth.info/smh#ae29bf58-0bbf-44bd-94dc-b59c5c987a51"||	"Established diagnosis of OSA"               |
  |"http://smartlifehealth.info/smh#e62bae64-2243-4ac0-ab19-9b411840e005"||	"DL107Da-Patients with Type 2 Diabetes"                            |
  |"http://smartlifehealth.info/smh#34eb2579-ca54-4493-96c8-9f3a565c8d79"||	"1. DL101-KPI-DEN-Patients on Diabetes QOF Register"|
  |"http://smartlifehealth.info/smh#0751a19c-1c33-4545-9f09-d44f86061b61"||	"DM017 - Patients on Diabetes QOF Register"     |
  |"http://smartlifehealth.info/smh#c2e078f9-73c6-4ec2-b3d0-19b4a7c7e51a"||	"RESP01D-Antibiotics and prednisolone 5mg tablets issued same day(after 01/04/24)"|
  |"http://smartlifehealth.info/smh#afef8cdb-7ec7-4a2e-91dc-03867909a78b"||	"RESP01D-Antibiotics and prednisolone 5mg tablets issued same day in last 12m"|
  |"http://smartlifehealth.info/smh#6e7b4e5f-1210-4a55-87f1-dbc03de82851"|                                                     |	"RESP01Nh-Education and self-management (in Financial Year)"                       |
  |"http://smartlifehealth.info/smh#f135bbd5-6317-46ce-84f6-24a2a5248033"||	"RESP01Ng-Support for Psychosocial wellbeing (in Financial Year)"          |
  |"http://smartlifehealth.info/smh#4e48b84c-6a2e-439e-a054-5bd9bbd05986"||	"RESP01Nf-Physical Activity (in Financial Year)"  |
  |"http://smartlifehealth.info/smh#46814ba9-c09a-4424-89d3-506c9e232ec4"|                                                                  |	"RESP01Ne-Offered Vaccine (in Financial Year)"|
  |"http://smartlifehealth.info/smh#581c7870-e060-4193-ba14-4de57a29e275"||	"RESP01Nd-Inhaler Technique (in Financial Year)"  |
  |"http://smartlifehealth.info/smh#f01a1443-8008-4193-8cb4-ec0285f7de57"|                                               |	"RESP01Nc-Tobacco dependence services (in Financial Year)"|
  |"http://smartlifehealth.info/smh#82dc058c-6c13-4720-bf1b-ddcfd8c7465d"|                                                             |	"RESP01Nb-Pulmonary Rehab (in Financial Year)"                |
  |"http://smartlifehealth.info/smh#7ba5bfb3-5dd6-4c6c-a580-c2158f75518c"||	"*RESP01D-ES-DEN-Patients in COPD OPTIMISE Cohort"                    |
  |"http://smartlifehealth.info/smh#1de6b1cb-7321-4ced-9c67-3378597b6109"|                                             |	"*RESP01N-ES-NUM-8 Care Processes (in Financial Year)"|
  |"http://smartlifehealth.info/smh#56a234e9-c023-4580-b640-50d98fe9d5ec"||	"RESP01Na-Optimise Treatment (in Financial Year)"|
  |"http://smartlifehealth.info/smh#18934bee-29d6-484c-af33-2c9313add5b9"||	"COPD015b-Unresolved COPD Diagnosis, spirometry below 0.7 after registration"                                               |
  |"http://smartlifehealth.info/smh#f7a2604e-4850-4fd4-99ef-b2ed700e2902"|                                                                  |	"COPD015a-Earliest unresolved COPD diagnosis"|
  |"http://smartlifehealth.info/smh#341cd4ca-381b-4133-b4a6-e54e7512d1d5"||	"AST005 - Patients on the asthma register"|
  |"http://smartlifehealth.info/smh#f5b4a7c9-9d8c-459e-8af0-6e5a3dbfacdb"||	"COPD015 - Patients on the COPD register"                                                                   |
  |"http://smartlifehealth.info/smh#d4e0a450-a0ee-4525-bce3-8c949f2cbffd"||	"RESP02D-ES-DEN-Patients in Asthma or COPD registers"|
  |"http://smartlifehealth.info/smh#df64348f-fd74-4cc0-990f-5d1a6d453d77"||	"RESP02N-ES-NUM-Patients with Inhaler Technique recorded (in Financial Yr)"                                                       |
  |"http://smartlifehealth.info/smh#78c7e8f8-e9de-4216-93ed-fe0fc213e3a5"||	"CRM00Ba  BETWEEN JAN LAST YR & MAR NEXT YEAR  First appointment"|
  |"http://smartlifehealth.info/smh#f4ba909c-00fd-4add-af1a-3b9fdbed76c6"|                                                |	"CRM00Bb  BETWEEN JAN LAST YR & MAR NEXT YEAR  Follow Up appointment"|
  |"http://smartlifehealth.info/smh#466df189-430e-48ae-9a8a-f120a2998705"||	"NHS NUMBERS  DQ  MISSING First Appointment -report"|
  |"http://smartlifehealth.info/smh#1d1f122f-370d-42b7-b2a0-1d02722bff97"||	"NHS NUMBERS  DQ  MISSING Follow Up appointment -report"                                                           |
  |"http://smartlifehealth.info/smh#618a843a-7993-4dad-bef5-3109cfcd09d8"||	"CRM02a  ALL CRM  ACHIEVED  LAST 15M TO END OF FY  HbA1c"|
  |"http://smartlifehealth.info/smh#cbc0dcda-420d-49e7-9df5-44b0a2c7e66f"||	"CRM02b  ALL CRM  ACHIEVED  LAST 15M TO END OF FY  Blood Pressure"|
  |"http://smartlifehealth.info/smh#845dd00d-aca5-4c75-b1a7-76c70e06d333"||	"CRM02c  ALL CRM  ACHIEVED  LAST 15M TO END OF FY  Lipids"|
  |"http://smartlifehealth.info/smh#8dae1292-4a3e-4eb1-b767-dff31dfe67d1"||	"CRM02d  ALL CRM  ACHIEVED  LAST 15M TO END OF FY  Urine ACR"                                                    |
  |"http://smartlifehealth.info/smh#21c147f9-d603-4164-879e-2807ffeeadb2"||	"CRM02e  ALL CRM  ACHIEVED  LAST 15M TO END OF FY  eGFR"|
  |"http://smartlifehealth.info/smh#9a3d6029-7320-4779-94c7-60dccb09f3f3"||	"CRM02f  ALL CRM  ACHIEVED  LAST 15M TO END OF FY  BMI"|
  |"http://smartlifehealth.info/smh#633d1231-c334-4a61-856a-e9ea08dffc05"||	"CRM02h  ALL CRM  ACHIEVED  LAST 15M TO END OF FY  Smoking Status"                                  |
  |"http://smartlifehealth.info/smh#b01fbd08-e2b9-4c73-ba77-31ce1f5eb720"||	"CRM02g  ALL CRM  ACHIEVED  LAST 15M TO END OF FY  Waist circumference"       |
  |"http://smartlifehealth.info/smh#80231269-1096-451f-85ee-11a319aa5c55"|                                                               |	"CRM02i  DIABETES  ACHIEVED  LAST 15M TO END OF FY  MH Screening"|
  |"http://smartlifehealth.info/smh#6f15fe46-4a92-4345-b1fd-be83d021282f"||	"CRM02j  DIABETES  ACHIEVED  LAST 15M TO END OF FY  Foot Check"|
  |"http://smartlifehealth.info/smh#9a4d0dc6-ea2c-48cd-8b6f-a4d156a2f05f"|                              |	"CRM02k  DIABETES  ACHIEVED  LAST 27M TO END OF FY  Retinal Screening"|
  |"http://smartlifehealth.info/smh#e3b1e82e-4e6e-4a27-baee-e1fc33ff3013"||	"CRM02l  DIABETES & MASLD  ACHIEVED  LAST 39M TO END OF FY  FIB-4"|
  |"http://smartlifehealth.info/smh#aa7ac323-9aa1-4262-9c78-20665320645d"|                                      |	"CRM02  ACHIEVED  Care Processes Completed"   |
  |"http://smartlifehealth.info/smh#9666497d-1792-4df4-9583-cbf3715509e4"||	"CRM03A  NOT FRAIL & AGE<80  ACHIEVED  LAST 15M TO END FY  Latest BP<=130/80"                                  |
  |"http://smartlifehealth.info/smh#c477afde-57c7-449e-8832-d3f9942b356e"||	"CRM03B  FRAIL & AGE>=80  ACHIEVED  LAST 15M TO END OF FY  Latest BP<=150/90"                 |
  |"http://smartlifehealth.info/smh#8e5c7851-9dbf-45c6-9bf5-96ab5abad937"|                                      |	"CRM07a  LAST 15M TO END OF FY  Care Plan"|
  |"http://smartlifehealth.info/smh#4f8379f1-e50c-450c-a4f6-3f87dde56ee4"||	"CRM07b  LAST 15M TO END OF FY  Eat"|
  |"http://smartlifehealth.info/smh#ff78f5c7-343b-4e6d-9523-ca322e4de435"||	"CRM07c  LAST 15M TO END OF FY  Physical Activity"|
  |"http://smartlifehealth.info/smh#50556f5c-0ecc-4d4f-9c96-235371022d24"|                                                           |	"CRM07d  LAST 15M TO END OF FY  Sleep Pattern"                                      |
  |"http://smartlifehealth.info/smh#437a2b19-61ec-4e98-a1eb-3d5046409199"|  |	"CRM07e  LAST 15M TO END OF FY  Relax"|
  |"http://smartlifehealth.info/smh#068c6d84-8915-423f-babf-a83827eb5128"|          |	"CRM07f  LAST 15M TO END OF FY  Connect"|
  |"http://smartlifehealth.info/smh#6da1e065-09aa-4b77-ba1d-7da7b30310e9"|        |	"CRM07g  LAST 15M TO END OF FY  Avoid harmful substances"|
  |"http://smartlifehealth.info/smh#e8fede1b-5578-486a-8e7e-26769bd5c087"||	"CRM07  ACHIEVEMENT  LAST 15M TO END OF FY  Holistic Care Plan completed"                                            |
  |"http://smartlifehealth.info/smh#7c6875fb-5f08-4ab2-a3cf-03b2574640d0"||	"CRM09a  ACHIEVED  LAST 15M TO END OF FY  Health Confidence Score"|
  |"http://smartlifehealth.info/smh#63e61f23-e874-4c84-a41f-927b838fa523"|                                                               |	"CRM09b  ACHIEVED  LAST 15M TO END OF FY  2 Health Confidence Scores 1m apart"             |
  |"http://smartlifehealth.info/smh#037c4fe2-3b1d-4de7-820d-3c26ad3603d1"||	"CRM10  DENOMINATOR  Diabetes QOF Register"                  |
  |"http://smartlifehealth.info/smh#f7c97eb3-13bb-4eae-9d53-9941c59f2aba"||	"CRM10c  NUMERATOR  LAST 15M  Latest Non HDL Cholesterol Ratio<=3"       |
  |"http://smartlifehealth.info/smh#44830aa6-ab2d-4a55-8c18-643b4c91a4be"||	"EMCRM10 -report"                                                       |
  |"http://smartlifehealth.info/smh#cefbd908-70ec-4373-8b98-65ab22ce768b"||	"Patients with Moderate/Severe Frailty or aged >= 80"|
  |"http://smartlifehealth.info/smh#869fd47b-8786-4d2c-8721-f332b6a2578d"||	"Patients with no Moderate/Severe Frailty or aged < 80"|
  |"http://smartlifehealth.info/smh#d96e01da-9fa4-48b9-86aa-38764dee661e"||	"CRM10a  NUMERATOR  LAST 15M  Latest HbA1c <= appropriate target"      |
  |"http://smartlifehealth.info/smh#902f795e-beb3-4185-9e68-79e0ece75eab"|                                                                  |	"CRM10b  NUMERATOR  LAST 15M  Latest BP <= appropriate target"                                                   |
  |"http://smartlifehealth.info/smh#5d07122a-a505-419e-9043-06ffe4e67bc8"||	"NHS NUMBERS  Patients with 3 Treatment Targets Checklist -report"|
  |"http://smartlifehealth.info/smh#f74255aa-7c65-424a-82aa-078f40eb309d"||	"NHS NUMBERS  Patients with 3 Treatment Targets Checklist -report"|
  |"http://smartlifehealth.info/smh#4dd72223-672f-4d4e-8b60-0c84b19b2e87"|                                           |	"*CRM10  ACHIEVEMENT  3 Treatment Targets Achieved"                                |
  |"http://smartlifehealth.info/smh#b92eaab1-407c-40ee-a7db-7feea84ce5f6"||	"CRM11D  DENOMINATOR  Aged  17-70  Diabetic patients diagnosed in last 2 yrs"|
  |"http://smartlifehealth.info/smh#c70183d2-e326-4fcf-9874-1a087caa272f"||	"CRM11N  ACHIEVED  LAST 15M  Latest HbA1c <= 48"|
  |"http://smartlifehealth.info/smh#c0b2e0d9-d2fb-4920-8426-cc31f1e1e127"||	"EMCRM11 -report"|
  |"http://smartlifehealth.info/smh#502b6f16-c646-41ff-b6aa-4f12ea39b7d3"||	"NHS NUMBERS  Diagnosed in last 2 yrs HbA1c <= 48 -report"|
  |"http://smartlifehealth.info/smh#d8251ce7-e1aa-46cb-9c2c-e2d24657669e"||	"CRM12D  DENOMINATOR  Hypertension  Aged < 80  Black & Black British patients"                                                                  |
  |"http://smartlifehealth.info/smh#18804ed7-5068-4652-bd50-ae417d693cfc"|                                          |	"CRM12N  ACHIEVED  LAST 12M  Latest BP <= 140/90"|
  |"http://smartlifehealth.info/smh#93e67f31-5d26-4f6c-8984-0acdefcc8103"||	"EMCRM12D -report"|
  |"http://smartlifehealth.info/smh#359abf37-ca7f-4f5d-b0c2-a1e7d7a7b5e3"|         |	"NHS NUMBERS  Patient level report"|
  |"http://smartlifehealth.info/smh#3de6be76-36de-4a64-bc4b-eef902fbd6b8"|             |	"EMCRM12N -report"|
  |"http://smartlifehealth.info/smh#eaa0f5cc-3e23-42d8-9be2-df418b1d00a1"|                                                  |	"CRM  AF QOF register  AF001 - Patients on the AF register"                                 |
  |"http://smartlifehealth.info/smh#224a8344-86be-4155-9b1e-e44e0e84a0be"|                 |	"CRM  AGE00a  Age  DOB between 17 and 28 years ago"|
  |"http://smartlifehealth.info/smh#7c78df37-1e31-4ad2-84f3-0a144d52037f"||	"CRM  AGE00b  Age  DOB between 17 and 30 years ago"|
  |"http://smartlifehealth.info/smh#d45d917c-a63c-455d-bec6-51850af5d5b6"||	"CRM  AGE00c  Age  DOB between 17 and 35 years ago"|
  |"http://smartlifehealth.info/smh#f4db6559-cf19-46d1-a4b7-4d9dcad192d4"||	"CRM  AGE00d  Age  DOB between 17 and 38 years ago"                                                                   |
  |"http://smartlifehealth.info/smh#2fe282e8-942c-4fcb-981f-53290a3e498f"|      |	"CRM  AGE00e  Age  DOB between 17 and 40 years ago"|
  |"http://smartlifehealth.info/smh#b1efaf88-9ecc-4a67-87ea-5ddd2b7a953b"|      |	"CRM  AGE00f  Age  DOB between 17 and 43 years ago"|
  |"http://smartlifehealth.info/smh#4bd8c16e-1ac8-4c28-a484-5b376cbab9f3"|      |	"CRM  AGE00g  Age  DOB between 17 and 45 years ago"|
  |"http://smartlifehealth.info/smh#23b34cf6-e588-4984-9288-3eece72ba5ce"|                         |	"CRM  AGE00h  Age  DOB between 17 and 50 years ago"|
  |"http://smartlifehealth.info/smh#02f1ec1a-7f30-4439-a9d9-60b0998916b1"||	"CRM  AGE00i  Age  DOB between 17 and 53 years ago"|
  |"http://smartlifehealth.info/smh#17fcd4ea-94a2-4217-9734-7ef251f2a129"|                                                              |	"CRM  AGE00j  Age  DOB between 17 and 55 years ago"                                                  |
  |"http://smartlifehealth.info/smh#2a853fe3-de6c-45cc-b3cb-f2040ddd56fd"||	"CRM  AGE00k  Age  DOB between 17 and 58 years ago"      |
  |"http://smartlifehealth.info/smh#bc2e4d08-7e4f-496a-907f-2867ea3a6d1f"||	"CRM  AGE00l  Age  DOB between 17 and 60 years ago"      |
  |"http://smartlifehealth.info/smh#b8307342-be1d-4e9b-a313-00d854490685"||	"CRM  AGE00m  Age  DOB between 17 and 65 years ago"      |
  |"http://smartlifehealth.info/smh#c135ae14-c152-4529-b6a2-edf9e3bc10a7"||	"CRM  AGE00n  Age  DOB between 17 and 70 years ago"                         |
  |"http://smartlifehealth.info/smh#d71bd6d0-95b8-4e0f-a9fb-7d64a4103011"||	"CRM  Antihypertensive medications  AHM01  Prescribed in last 6 months"                                                               |
  |"http://smartlifehealth.info/smh#50292e41-b060-4e2c-9970-6a8bbea36fb9"|                                                                 |	"CRM  BMI00a  BMI > 50"                                                        |
  |"http://smartlifehealth.info/smh#f1bd9c4e-5f4d-4730-a434-20a4edcb9a1f"||	"CRM  BMI00b  BMI > 48"          |
  |"http://smartlifehealth.info/smh#9e6e64ce-d0b1-47e6-a4bb-19fc15e0c0fe"|               |	"CRM  BMI00c  BMI > 45"    |
  |"http://smartlifehealth.info/smh#c14fc528-a6db-49fc-a27c-f53010f9f414"|                          |	"CRM  BMI00d  BMI > 43"   |
  |"http://smartlifehealth.info/smh#ef78878b-03e5-4b65-8a47-001df29cb783"|                          |	"CRM  BMI00e  BMI > 40"   |
  |"http://smartlifehealth.info/smh#dfcae4cc-2812-48d8-ac73-9b314b8c9093"|                                           |	"CRM  BMI00f  BMI > 38"|
  |"http://smartlifehealth.info/smh#227b9b9e-bb95-4d76-a912-d31b5b63e2a6"|             |	"CRM  BMI00g  BMI > 35"  |
  |"http://smartlifehealth.info/smh#9226d7d7-76a1-4d26-9abf-bbe063921895"|             |	"CRM  BMI00h  BMI > 33"|
  |"http://smartlifehealth.info/smh#c5913224-c095-4707-b062-602af5959d7c"|      |	"CRM  BMI00i  BMI > 30"|
  |"http://smartlifehealth.info/smh#9b4bbf76-7f42-4c38-b186-f4aa1d8effd1"|      |	"CRM  BMI00j  BMI > 28"                |
  |"http://smartlifehealth.info/smh#9a7ec18d-9328-41a8-8fc6-69d18d501533"|      |	"CRM  BP001a  Systolic Blood Pressure > 179"|
  |"http://smartlifehealth.info/smh#9121e6f0-6ddd-457f-ad6f-be4f5448cfe9"||	"CRM  BP001b  Systolic Blood Pressure > 162"                     |
  |"http://smartlifehealth.info/smh#fe9b8e8d-1659-410e-8b9d-9994f30af869"||	"CRM  BP001c  Systolic Blood Pressure > 145"|
  |"http://smartlifehealth.info/smh#9c7d1ad7-724d-471c-acb3-d8c27d91de58"|                                                           |	"CRM  CHOL00a  Cholesterol:HDL Ratio > 8.5"                                 |
  |"http://smartlifehealth.info/smh#8516f3f1-5f9d-4855-8eb3-94505124dacb"||	"CRM  CHOL00b  Cholesterol:HDL Ratio > 8.0"|
  |"http://smartlifehealth.info/smh#0f3c0bcb-bf88-4ba8-8eab-e411a3550f00"||	"CRM  CHOL00c  Cholesterol:HDL Ratio > 7.5"|
  |"http://smartlifehealth.info/smh#f58e7b0e-3d5b-473f-a9df-51d39f08e73a"||	"CRM  CHOL00d  Cholesterol:HDL Ratio > 7.0"                                                                 |
  |"http://smartlifehealth.info/smh#69d6d9df-8894-4a5b-a1de-c4af8a7e16ce"||	"CRM  CHOL00e  Cholesterol:HDL Ratio > 6.5"|
  |"http://smartlifehealth.info/smh#047eeee2-0183-4bf9-bb4d-5b6ca7960b5b"|  |	"CRM  CHOL00f  Cholesterol:HDL Ratio > 6.0"|
  |"http://smartlifehealth.info/smh#098ac7ab-96a9-4d87-b477-2bcb06462069"|    |	"CRM  CHOL00g  Cholesterol:HDL Ratio > 5.5"|
  |"http://smartlifehealth.info/smh#47c31bbb-62ce-4bbb-8bc8-f956f06faa3b"||	"CRM  CHOL00h  Cholesterol:HDL Ratio > 5.0"|
  |"http://smartlifehealth.info/smh#212b243f-6d11-4147-99ac-2ca934e8f63e"|     |	"CRM  CHOL00i  Cholesterol:HDL Ratio > 4.5"|
  |"http://smartlifehealth.info/smh#af5206b9-577f-47e8-b66d-0a2a64ef5ef2"||	"CRM  CHOL00j  Cholesterol:HDL Ratio > 4.0"|
  |"http://smartlifehealth.info/smh#4d54c09a-a161-4843-a465-2b44fe363fe0"|                                                             |	"CRM  CKD QOF register  CKD005 - Patients on the CKD register"        |
  |"http://smartlifehealth.info/smh#36362758-b553-4ca5-939e-326c7c6ade4d"||	"CRM  CKD Undiagnosed  CKD01Dd  Latest eGFR<60 & 2nd eGFR<60 btwn 3m &2yrs ago"|
  |"http://smartlifehealth.info/smh#af4c5307-6986-4d4b-a4bb-3654d829c8b7"||	"CRM  CKD Undiagnosed  CKD01De  uACR> 3 & 2nd uACR>3 btwn 1 wk & 2 yrs ago"                                       |
  |"http://smartlifehealth.info/smh#15b3ce63-1336-409c-8aec-1c9bf401a00d"|                               |	"CRM  DM  Patient having unresolved diabetes code"                                             |
  |"http://smartlifehealth.info/smh#9f9268dc-e578-42c2-95dd-eba5f12dd495"|                                                                     |	"CRM  ETH00b  South Asian Ethnicity"|
  |"http://smartlifehealth.info/smh#44b0637d-ba3d-4d4f-afd5-c475b7c7f503"||	"CRM  FHFD01  Family history of heart disease < 60"|
  |"http://smartlifehealth.info/smh#fa428662-800f-41a8-a78f-c94671403978"|                                                      |	"CRM  Mental health QOF register  MH1_REG - Psychosis, schizophrenia or bipolar"                                                            |
  |"http://smartlifehealth.info/smh#f54117b6-8653-4541-b663-d222aabee65b"|                                       |	"CRM  Mental health QOF register  MH2_REG - Lithium treatment in last 6m"          |
  |"http://smartlifehealth.info/smh#780cbec3-7a82-43e7-b6a1-5d673d69e531"|                                                                   |	"CRM  SMOK01a  Heavy Smoker"                           |
  |"http://smartlifehealth.info/smh#e07d9db8-22f5-43d8-a8f5-3a4e937e5bf3"|               |	"CRM  SMOK01b  Moderate or Unclassified Smoker"                                                            |
  |"http://smartlifehealth.info/smh#55a8c827-b8f5-4797-9fa4-38b54282f233"||	"CRM  SMOK01c  Light Smoker"|
  |"http://smartlifehealth.info/smh#bece273e-71fc-46cc-bde4-85fa5b6b80e4"|          |	"CRM  Systemic lupus erythematosus"|
  |"http://smartlifehealth.info/smh#a577cf44-d4e4-4244-8604-a59bb81f85c4"|       |	"RISK00b  Gender  Male"     |
  |"http://smartlifehealth.info/smh#e5fdd1f0-c454-4eea-96f6-ea05225710ca"|                   |	"RISK00c  Ethnicity  Black & Black British patients"|
  |"http://smartlifehealth.info/smh#a80afdfa-c62b-46a2-9587-98c952cd7311"||	"RISK00j  Rheumatoid arthritis"|
  |"http://smartlifehealth.info/smh#d411f62b-5b8f-4f91-82e4-4c142148189a"| |	"RISK00l  Migraine"     |
  |"http://smartlifehealth.info/smh#551d25a9-5935-4376-abce-40ffba3cfffb"|                       |	"RISK00gd  Atrial Fibrillation  Age < 35"|
  |"http://smartlifehealth.info/smh#9a89ad06-c0f2-46a5-8e34-9e592e0f4045"| |	"RISK00ge  Atrial Fibrillation  Age < 30"|
  |"http://smartlifehealth.info/smh#a9874692-d7f9-4886-aa2e-f971daee4279"| |	"RISK00gc  Atrial Fibrillation  Age < 40"|
  |"http://smartlifehealth.info/smh#1e3a4b90-52ff-478e-b93b-e854825995ec"| |	"RISK00gb  Atrial Fibrillation  Age < 50"|
  |"http://smartlifehealth.info/smh#8b312447-57d3-427e-8819-8ccd15c7d5b8"| |	"RISK00ac  Age  Age < 60"|
  |"http://smartlifehealth.info/smh#73b3a378-8a0a-4a21-b0be-02b630664ba4"|                                    |	"RISK00ga  Atrial Fibrillation  Age < 60"|
  |"http://smartlifehealth.info/smh#aa455e87-b66c-4bcf-a79f-28215e29307c"|                                     |	"RISK00ab  Age  Age < 65"                                                           |
  |"http://smartlifehealth.info/smh#531375a7-c4e0-449a-b57b-598d0bcc4c36"|                                                 |	"RISK00aa  Age  Age < 70"|
  |"http://smartlifehealth.info/smh#8d15e153-bc9e-4542-b7bc-3e0ec911a78e"|                        |	"RISK00ha  Antihypertensive medication  Age < 65"|
  |"http://smartlifehealth.info/smh#e2834d2c-6a39-4291-b875-fc4f89281585"||	"RISK00hb  Antihypertensive medication  Age < 58"                 |
  |"http://smartlifehealth.info/smh#06e5adb5-0af0-4f02-a40f-c718dcb5b09d"||	"RISK00hc  Antihypertensive medication  Age < 50"|
  |"http://smartlifehealth.info/smh#200b4319-be0f-4cf4-8e2a-d236aadf2577"|                 |	"RISK00hd  Antihypertensive medication  Age < 43"|
  |"http://smartlifehealth.info/smh#7b68c542-f606-4512-92ae-6162652cf285"|                       |	"RISK00he  Antihypertensive medication  Age < 35"|
  |"http://smartlifehealth.info/smh#3db1ffbf-087e-426a-b8d8-c981d269d510"||	"RISK00hf  Antihypertensive medication  Age < 28"|
  |"http://smartlifehealth.info/smh#3c85bcd6-1504-451d-996b-320177d83bd2"||	"RISK00ra  Systolic Blood Pressure > 179"        |
  |"http://smartlifehealth.info/smh#48ce7ac6-d938-4088-a3b4-149e8ab1d16c"||	"RISK00rb  Systolic Blood Pressure > 179  Age < 50"|
  |"http://smartlifehealth.info/smh#dee00a85-a115-4496-a579-ddd2493cdcda"||	"RISK00rc  Systolic Blood Pressure > 162"|
  |"http://smartlifehealth.info/smh#823e8415-3df0-40d3-a098-9e01bf850b56"||	"RISK00rd  Systolic Blood Pressure > 162  Age < 50"|
  |"http://smartlifehealth.info/smh#95ece6ea-c6a6-4761-996e-1aa8d0ccdda8"||	"RISK00re  Systolic Blood Pressure > 162  Age < 35"|
  |"http://smartlifehealth.info/smh#ea1b4a06-b0a2-4b6b-98f7-662e44a178ee"||	"RISK00rf  Systolic Blood Pressure > 145"|
  |"http://smartlifehealth.info/smh#a773f0bf-fac6-4f20-91e5-d832015c7084"||	"RISK00rg  Systolic Blood Pressure > 145  Age < 50"|
  |"http://smartlifehealth.info/smh#0e861f0d-2650-45f1-9c61-d0b63f94d59c"||	"RISK00rh  Systolic Blood Pressure > 145  Age < 35"|
  |"http://smartlifehealth.info/smh#6ef2ba8a-5b16-47f7-90eb-b2fb4e6c7b88"||	"RISK00pa  Cholesterol:HDL Ratio > 8.5"|
  |"http://smartlifehealth.info/smh#326815cd-1f08-4ae1-9bc1-d817895343ae"||	"RISK00pb  Cholesterol:HDL Ratio > 8.0"|
  |"http://smartlifehealth.info/smh#65e338cc-73b7-4b8d-95d3-042e4145c25e"||	"RISK00qa  Cholesterol:HDL Ratio > 8.0  Age < 53"|
  |"http://smartlifehealth.info/smh#fcd15d35-313f-4c99-97e3-b067003682ba"||	"RISK00pc  Cholesterol:HDL Ratio > 7.5"|
  |"http://smartlifehealth.info/smh#4333da38-309f-46b0-a4ca-3ada05480247"||	"RISK00pd  Cholesterol:HDL Ratio > 7.0"|
  |"http://smartlifehealth.info/smh#2c1f7c6f-e7f7-4359-ae3e-846de2bb0964"||	"RISK00qb  Cholesterol:HDL Ratio > 7.0  Age < 53"|
  |"http://smartlifehealth.info/smh#31b73cfd-6995-4951-ae3a-e6e01e289269"||	"RISK00pe  Cholesterol:HDL Ratio > 6.5"|
  |"http://smartlifehealth.info/smh#73ebdb65-19a2-48d1-aae0-68b9cebe6d58"||	"RISK00pf  Cholesterol:HDL Ratio > 6.0"|
  |"http://smartlifehealth.info/smh#b9859ca4-3e49-4a24-8ee7-310b700a0260"||	"RISK00qc  Cholesterol:HDL Ratio > 6.0  Age < 53"|
  |"http://smartlifehealth.info/smh#58e3a540-48c7-4f80-a7ac-6b0ba57f3f9c"||	"RISK00pg  Cholesterol:HDL Ratio > 5.5"|
  |"http://smartlifehealth.info/smh#7c5e03f0-dd81-47df-8359-ea761db4d7c0"||	"RISK00ph  Cholesterol:HDL Ratio > 5.0"|
  |"http://smartlifehealth.info/smh#a659c8c5-262c-407d-95c0-5d076ab18047"||	"RISK00qd  Cholesterol:HDL Ratio > 5.0  Age < 53"|
  |"http://smartlifehealth.info/smh#b782a0b8-5b58-4942-a61b-b46972a7eee4"||	"RISK00pi  Cholesterol:HDL Ratio > 4.5"|
  |"http://smartlifehealth.info/smh#9a01d1f6-6b0b-4cbe-b8a7-e7559954ef00"||	"RISK00pj  Cholesterol:HDL Ratio > 4.0"|
  |"http://smartlifehealth.info/smh#eb89f57a-d309-4758-9d4e-ac01ad581c03"||	"RISK00qe  Cholesterol:HDL Ratio > 4.0  Age < 53"|
  |"http://smartlifehealth.info/smh#177d1801-447f-49f4-b3ba-7b82038fb50e"||	"CRM  CKD Undiagnosed  CKD01Df  Patients with 2* eGFR<60 or 2*uACR>3"|
  |"http://smartlifehealth.info/smh#de32c6ec-de87-4f29-9552-0df93bdb698a"||	"CRM  DM QOF register  DM017 - Patients > = aged 17 on the diabetes register"|
  |"http://smartlifehealth.info/smh#c7030c8a-5ecb-4a41-90b3-eef1c3fc28fd"||	"CRM  ETH00a  Not South Asian Ethnicity"|
  |"http://smartlifehealth.info/smh#6f8de906-827f-42e7-b746-680c341a4e24"||	"RISK00ea  Family history of heart disease < 60  Age < 60"|
  |"http://smartlifehealth.info/smh#651d06a6-e14c-4582-906b-c26e9cf2afd7"||	"RISK00eb  Family history of heart disease < 60  Age < 53"|
  |"http://smartlifehealth.info/smh#661aeb80-8179-4d06-90e5-bdf1dae2a818"||	"RISK00ec  Family history of heart disease < 60  Age < 45"|
  |"http://smartlifehealth.info/smh#957ab762-ff4e-4168-8235-eae76799f5ca"||	"RISK00ed  Family history of heart disease < 60  Age < 38"|
  |"http://smartlifehealth.info/smh#3d03d88e-83d2-48a4-859f-158a969594ac"||	"RISK00ee  Family history of heart disease < 60  Age < 30"|
  |"http://smartlifehealth.info/smh#537a9978-8339-47c9-a6c1-1c2b9ef143fb"||	"CRM  Mental health QOF register  MH001 - Patients on mental health register"|
  |"http://smartlifehealth.info/smh#44d51b50-c3ec-4cd2-828a-cbada1bf538a"||	"RISK00ma  Heavy Smoker"|
  |"http://smartlifehealth.info/smh#9f457260-3a6f-4ff2-97bb-a8c78ed01d4d"||	"RISK00mb  Heavy Smoker  Age < 65"|
  |"http://smartlifehealth.info/smh#53f896d5-868d-49ea-8de2-b9fc6b6b7cd9"||	"RISK00mc  Heavy Smoker  Age < 53"|
  |"http://smartlifehealth.info/smh#87ac7727-b6ff-4ec1-abf9-c7dfaaa0dd18"||	"RISK00md  Heavy Smoker  Age < 38"|
  |"http://smartlifehealth.info/smh#2f046cbd-bc37-487a-a6d3-e1fa07be2e41"||	"RISK00me  Heavy Smoker  Age < 30"|
  |"http://smartlifehealth.info/smh#344ce528-2218-4ab9-b0d9-35a0195ec083"||	"RISK00na  Moderate or Unclassified Smoker"|
  |"http://smartlifehealth.info/smh#dc5f3031-e183-417f-a5a6-4ec50c8160d0"||	"RISK00nb  Moderate or Unclassified Smoker  Age < 65"|
  |"http://smartlifehealth.info/smh#518a039a-a3de-4b66-98d3-04ef857d2d2d"||	"RISK00nc  Moderate or Unclassified Smoker  Age < 53"|
  |"http://smartlifehealth.info/smh#2af07a12-b173-4341-8d14-705664071e75"||	"RISK00nd  Moderate or Unclassified Smoker  Age < 38"|
  |"http://smartlifehealth.info/smh#60d7692d-fab0-4173-b137-85a8eb70bf81"||	"RISK00ne  Moderate or Unclassified Smoker  Age < 30"|
  |"http://smartlifehealth.info/smh#ffdb9a58-9b2f-4bca-a113-466245ad6d1f"||	"RISK00o  Light Smoker"|
  |"http://smartlifehealth.info/smh#8aaf3210-4c33-40be-b9ef-f9315f0c8c1b"||	"RISK00ia  Systemic lupus erythematosus"|
  |"http://smartlifehealth.info/smh#e6920629-ee93-4bab-96ff-58e103647980"||	"RISK00ib  Systemic lupus erythematosus  Age < 45"|
  |"http://smartlifehealth.info/smh#b42c9cda-a613-4570-9de3-d99a00c58683"||	"RISK00ic  Systemic lupus erythematosus  Age < 30"|
  |"http://smartlifehealth.info/smh#1e2257ff-401f-45a9-b813-3b6f90c728ff"||	"CRM  CKD Undiagnosed  CKD01D  Patients who are likely to have CKD"|
  |"http://smartlifehealth.info/smh#3a06fa1d-c2d3-4709-81a5-d6edbfe323df"||	"RISK00da  Diabetes  Type 1 Diabetes"|
  |"http://smartlifehealth.info/smh#5f7a2f0d-da37-4d4e-8046-4cdc298feb46"||	"RISK00db  Diabetes  Type 2 Diabetes"|
  |"http://smartlifehealth.info/smh#15036062-5e73-455a-8287-9f2f38e76827"||	"RISK00dc  Diabetes  Age < 65"|
  |"http://smartlifehealth.info/smh#0acf0831-881b-48db-a96c-2f2d663b6dd2"||	"RISK00dd  Diabetes  Age < 60"|
  |"http://smartlifehealth.info/smh#5e1361ae-a1f2-4ad0-8c7f-206bdd5a8056"||	"RISK00de  Diabetes  Age < 55"|
  |"http://smartlifehealth.info/smh#aa25660d-d8db-452d-8062-cc16aa329bfd"||	"RISK00df  Diabetes  Age < 50"|
  |"http://smartlifehealth.info/smh#9409ad26-bb59-4639-89dd-03c4659d03cb"||	"RISK00dg  Diabetes  Age < 45"|
  |"http://smartlifehealth.info/smh#455dd5af-1a2b-4856-b4f5-378c74bbf2bc"||	"RISK00dh  Diabetes  Age < 40"|
  |"http://smartlifehealth.info/smh#fe27b3c1-a241-47e9-b3fa-673233d21060"||	"RISK00di  Diabetes  Age < 35"|
  |"http://smartlifehealth.info/smh#db4d043e-055d-4dcc-acfc-e145ecf98d87"||	"RISK00dj  Diabetes  Age < 30"|
  |"http://smartlifehealth.info/smh#186d3bc1-723f-41aa-817e-f37bfef9d1b8"||	"RISK00sa  BMI > 50 or South Asian and BMI > 48"|
  |"http://smartlifehealth.info/smh#b95408be-16a4-4da5-8cd8-d2987f8db66d"||	"RISK00sb  BMI > 45 or South Asian and BMI > 43"|
  |"http://smartlifehealth.info/smh#87e758b7-b7e7-4ddf-a5b2-faec64067d8d"||	"RISK00sc  BMI > 40 or South Asian and BMI > 38"|
  |"http://smartlifehealth.info/smh#98fc820e-27cf-4e6c-b93e-07bd58651cc9"||	"RISK00sd  BMI > 35 or South Asian and BMI > 33"|
  |"http://smartlifehealth.info/smh#7dd09875-e56c-4fb5-bb30-f8981065cbd6"||	"RISK00se  BMI > 30 or South Asian and BMI > 28"|
  |"http://smartlifehealth.info/smh#eba039d0-3827-409c-8c81-ee86a4d0399c"||	"RISK00k  Serious Mental Illness"|
  |"http://smartlifehealth.info/smh#4ddba8a5-253e-4052-86e3-138b07906f4e"||	"CRM  CKD QOF register OR CKD undiagnosed  CKD005 or CKD01D"|
  |"http://smartlifehealth.info/smh#db9daa1f-ffe3-4fc8-8d4f-b6122dd4f058"||	"RISK00fa  CKD  Age < 65"|
  |"http://smartlifehealth.info/smh#ec07fa2e-ab58-4751-a61a-0342994ed983"||	"RISK00fb  CKD  Age < 58"|
  |"http://smartlifehealth.info/smh#2f08d101-dd3d-48da-952e-0fd5985e7abe"||	"RISK00fc  CKD  Age < 50"|
  |"http://smartlifehealth.info/smh#cb038ecf-9da5-4f5b-af8d-3a3904cff1ed"||	"CRM  AF QOF register  AF001 - Patients on the AF register"|
  |"http://smartlifehealth.info/smh#7db3a679-d4d7-4670-99b7-448a97df02b1"||	"CRM  Antihypertensive medications  AHM01  Prescribed in last 6 months"|
  |"http://smartlifehealth.info/smh#b36039f9-316d-44e6-8dc0-0b2dc727b9fb"||	"CRM  BMI00a  BMI > 50"|
  |"http://smartlifehealth.info/smh#75bb4d32-687b-4ece-beea-0a2343f3655c"||	"CRM  BMI00b  BMI > 48"|
  |"http://smartlifehealth.info/smh#2c1a1dbb-4117-43f5-b256-2f95bd16e6ee"||	"CRM  BMI00c  BMI > 45"|
  |"http://smartlifehealth.info/smh#f448fb03-5559-4d43-8013-4dd48b0254d7"||	"CRM  BMI00d  BMI > 43"|
  |"http://smartlifehealth.info/smh#85c91b70-8a90-47e8-bdbe-8fea23fcd9df"||	"CRM  BMI00e  BMI > 40"|
  |"http://smartlifehealth.info/smh#a6c43e44-6447-45c2-9253-b68f8751dbde"||	"CRM  BMI00f  BMI > 38"|
  |"http://smartlifehealth.info/smh#5f3bdcda-2ef1-4859-ba14-7526be0eea3f"||	"CRM  BMI00g  BMI > 35"|
  |"http://smartlifehealth.info/smh#b0c26d2a-0e82-42bc-b6e6-27325bd9b217"||	"CRM  BMI00h  BMI > 33"|
  |"http://smartlifehealth.info/smh#d281d655-fbaf-4587-82f2-1269dba58bb1"||	"CRM  BMI00i  BMI > 30"|
  |"http://smartlifehealth.info/smh#6fa2ca0d-78d0-469a-bdbf-6dfe795540b1"||	"CRM  BMI00j  BMI > 28"|
  |"http://smartlifehealth.info/smh#7fd6acc5-607f-4f1e-97ae-2bdf8952c32a"||	"CRM  BP001a  Systolic Blood Pressure > 179"|
  |"http://smartlifehealth.info/smh#596930bb-7394-436c-81ec-a10c0dc89ebf"||	"CRM  BP001b  Systolic Blood Pressure > 162"|
  |"http://smartlifehealth.info/smh#c8dad7c8-db6b-4473-ac04-70600fb83427"||	"CRM  BP001c  Systolic Blood Pressure > 145"|
  |"http://smartlifehealth.info/smh#0820e413-a868-4ddc-8873-898d313d968d"||	"CRM  CHOL00a  Cholesterol:HDL Ratio > 8.5"|
  |"http://smartlifehealth.info/smh#cbd8ec9c-a9b7-4428-9fb4-d8a8c39c39a2"||	"CRM  CHOL00b  Cholesterol:HDL Ratio > 8.0"|
  |"http://smartlifehealth.info/smh#f549f82e-601c-4278-9dc3-a6632b26dc39"||	"CRM  CHOL00c  Cholesterol:HDL Ratio > 7.5"|
  |"http://smartlifehealth.info/smh#48603575-d6b7-4be5-af21-f75a45bd332d"||	"CRM  CHOL00d  Cholesterol:HDL Ratio > 7.0"|
  |"http://smartlifehealth.info/smh#e6c6a16a-13ec-4074-8e85-49f7950e1292"||	"CRM  CHOL00e  Cholesterol:HDL Ratio > 6.5"|
  |"http://smartlifehealth.info/smh#023b82b6-1cc7-446a-9486-d5aeb3cdafcd"||	"CRM  CHOL00f  Cholesterol:HDL Ratio > 6.0"|
  |"http://smartlifehealth.info/smh#3ae7adff-d813-4316-8058-8ed12d909ec7"||	"CRM  CHOL00g  Cholesterol:HDL Ratio > 5.5"|
  |"http://smartlifehealth.info/smh#44ca9005-ccfa-495b-819c-27efe4546426"||	"CRM  CHOL00h  Cholesterol:HDL Ratio > 5.0"|
  |"http://smartlifehealth.info/smh#54dc9c87-1ea0-4f7b-8579-aabb288572e6"||	"CRM  CHOL00i  Cholesterol:HDL Ratio > 4.5"|
  |"http://smartlifehealth.info/smh#f310f8fd-bfb5-4b29-97e3-6900445c03b3"||	"CRM  CHOL00j  Cholesterol:HDL Ratio > 4.0"|
  |"http://smartlifehealth.info/smh#48eff18d-50fe-44a1-8a82-3c91ed069beb"||	"CRM  CKD QOF register  CKD005 - Patients on the CKD register"|
  |"http://smartlifehealth.info/smh#1da57f7e-326d-4dad-ae64-5f8425ec1c48"||	"CRM  CKD Undiagnosed  CKD01Dd  Latest eGFR<60 & 2nd eGFR<60 btwn 3m &2yrs ago"|
  |"http://smartlifehealth.info/smh#af3f054a-72cd-4f02-a1bd-4a4411acb9c1"||	"CRM  CKD Undiagnosed  CKD01De  uACR> 3 & 2nd uACR>3 btwn 1 wk & 2 yrs ago"|
  |"http://smartlifehealth.info/smh#38fd03c3-3203-4c5f-901a-648554654e53"||	"CRM  DM  Patient having unresolved diabetes code"|
  |"http://smartlifehealth.info/smh#5671ee5a-a747-42f5-a242-092d193d97ef"||	"CRM  ETH00b  South Asian Ethnicity"|
  |"http://smartlifehealth.info/smh#bf2bca46-900d-4905-847e-224ce1e88097"||	"CRM  FHFD01  Family history of heart disease < 60"|
  |"http://smartlifehealth.info/smh#4608b6cf-a574-4e11-967a-a0a37a462907"||	"CRM  Mental health QOF register  MH1_REG - Psychosis, schizophrenia or bipolar"|
  |"http://smartlifehealth.info/smh#8fbb5949-1181-4297-89a3-0d7325c8f76d"||	"CRM  Mental health QOF register  MH2_REG - Lithium treatment in last 6m"|
  |"http://smartlifehealth.info/smh#858ec01e-324e-4171-b1ef-c90140bcb28f"||	"CRM  SMOK01a  Heavy Smoker"|
  |"http://smartlifehealth.info/smh#0626b3cd-b108-4241-8186-458653e0a94c"||	"CRM  SMOK01b  Moderate or Unclassified Smoker"|
  |"http://smartlifehealth.info/smh#82a09201-c1d6-4d8e-9a30-106b4fba3905"||	"CRM  SMOK01c  Light Smoker"|
  |"http://smartlifehealth.info/smh#fde787c6-e107-419c-b245-00a2ae1c3518"||	"CRM  Systemic lupus erythematosus"|
  |"http://smartlifehealth.info/smh#5ee27250-68f7-4ff8-a70f-1f47c1d2242b"||	"RISK00b  Gender  Male"|
  |"http://smartlifehealth.info/smh#ecea2f78-e0f6-4b51-8bff-fe4abccfd5ef"||	"RISK00c  Ethnicity  Black & Black British patients"                                                                      |
  |"http://smartlifehealth.info/smh#d3b049b0-2a9d-4211-bba8-d09cf6db4a84"||	"RISK00j  Rheumatoid arthritis"|
  |"http://smartlifehealth.info/smh#6a0ebba0-6995-4b73-a951-bf8b670bcd98"||	"RISK00l  Migraine"|
  |"http://smartlifehealth.info/smh#00ef8ab4-d03a-474e-93dd-c022c916c000"||	"RISK00ac  Age  Age < 60"|
  |"http://smartlifehealth.info/smh#cd2b6b1d-00b9-4310-a740-947bbde70839"||	"RISK00ab  Age  Age < 65"|
  |"http://smartlifehealth.info/smh#d25557f6-1f3e-44e2-ac4e-76140e7cbe18"||	"RISK00aa  Age  Age < 70"|
  |"http://smartlifehealth.info/smh#260311f9-15d0-412e-9066-25080b2872f0"||	"RISK00ga  Atrial Fibrillation  Age < 60"|
  |"http://smartlifehealth.info/smh#06825030-aeef-474c-a0f0-3831b755cfea"||	"RISK00gb  Atrial Fibrillation  Age < 50"|
  |"http://smartlifehealth.info/smh#9d0793b1-fa41-4e22-8372-fed878990be8"||	"RISK00gc  Atrial Fibrillation  Age < 40"|
  |"http://smartlifehealth.info/smh#a88877e4-5c5f-45be-a673-abc31347ea2a"||	"RISK00gd  Atrial Fibrillation  Age < 35"|
  |"http://smartlifehealth.info/smh#14e67739-58b9-45ed-b6e1-09d55c9e4afc"||	"RISK00ge  Atrial Fibrillation  Age < 30"|
  |"http://smartlifehealth.info/smh#22128939-dbe8-41ba-887c-9cf068d8fba3"||	"RISK00ha  Antihypertensive medication  Age < 65"|
  |"http://smartlifehealth.info/smh#c2ec7f23-ecfe-47c7-8083-373d30a21ead"||	"RISK00hb  Antihypertensive medication  Age < 58"|
  |"http://smartlifehealth.info/smh#075f9133-1a3c-408f-b49d-dcc51f3bc433"||	"RISK00hc  Antihypertensive medication  Age < 50"|
  |"http://smartlifehealth.info/smh#666a07c6-bdb3-42e0-b956-ead2a35c1810"||	"RISK00hd  Antihypertensive medication  Age < 43"|
  |"http://smartlifehealth.info/smh#f5e34f73-2884-4c38-a24f-78ac4345f92a"||	"RISK00he  Antihypertensive medication  Age < 35"|
  |"http://smartlifehealth.info/smh#df6d02be-799f-45d5-a1a5-d89ee684180a"||	"RISK00hf  Antihypertensive medication  Age < 28"|
  |"http://smartlifehealth.info/smh#d4be6510-07b5-4b1e-8a0c-64d78277f17e"||	"RISK00ra  Systolic Blood Pressure > 179"|
  |"http://smartlifehealth.info/smh#26f063d7-3d10-45e9-ae7c-3646f6dd0f64"||	"RISK00rb  Systolic Blood Pressure > 179  Age < 50"|
  |"http://smartlifehealth.info/smh#73c63458-8b44-479a-975f-cbcdf600b488"||	"RISK00rc  Systolic Blood Pressure > 162"|
  |"http://smartlifehealth.info/smh#1e21f99b-0afe-4836-a0b6-45684c449dfc"||	"RISK00rd  Systolic Blood Pressure > 162  Age < 50"|
  |"http://smartlifehealth.info/smh#72a6f91b-fcd3-41d6-9bf5-b149fcc05151"||	"RISK00re  Systolic Blood Pressure > 162  Age < 35"|
  |"http://smartlifehealth.info/smh#0513c8c9-e128-49c5-bcc5-b667e85ebf68"||	"RISK00rf  Systolic Blood Pressure > 145"|
  |"http://smartlifehealth.info/smh#79a9af40-02a4-46ac-b577-041964111288"||	"RISK00rg  Systolic Blood Pressure > 145  Age < 50"|
  |"http://smartlifehealth.info/smh#7604b23b-3638-4a8f-ab09-00b1f06b4030"||	"RISK00rh  Systolic Blood Pressure > 145  Age < 35"|
  |"http://smartlifehealth.info/smh#b1da0899-d344-4a4a-9b2d-b65405284513"||	"RISK00pa  Cholesterol:HDL Ratio > 8.5"|
  |"http://smartlifehealth.info/smh#4397a0c1-2a61-47db-9c75-c3351bbf73f0"||	"RISK00pb  Cholesterol:HDL Ratio > 8.0"|
  |"http://smartlifehealth.info/smh#aa6d59d7-dcda-4b0d-ad69-d21edf4f8c5c"||	"RISK00qa  Cholesterol:HDL Ratio > 8.0  Age < 53"|
  |"http://smartlifehealth.info/smh#bbfce98b-9231-495b-9dd3-752b67986eac"||	"RISK00pc  Cholesterol:HDL Ratio > 7.5"|
  |"http://smartlifehealth.info/smh#08528300-4b19-4f67-a7c6-b7b96b220fa5"||	"RISK00pd  Cholesterol:HDL Ratio > 7.0"|
  |"http://smartlifehealth.info/smh#bc89dcb1-62e0-4f08-adee-939bbd33435f"||	"RISK00qb  Cholesterol:HDL Ratio > 7.0  Age < 53"|
  |"http://smartlifehealth.info/smh#c5eab871-68e7-4d14-90ef-2a1915bd325b"||	"RISK00pe  Cholesterol:HDL Ratio > 6.5"|
  |"http://smartlifehealth.info/smh#901d6742-db9f-426c-a3ee-a1bee309a872"||	"RISK00pf  Cholesterol:HDL Ratio > 6.0"|
  |"http://smartlifehealth.info/smh#123dd8cb-7e59-4c7a-8b9a-f8bf9a8713ec"||	"RISK00qc  Cholesterol:HDL Ratio > 6.0  Age < 53"|
  |"http://smartlifehealth.info/smh#8b20f9d6-cb6c-476f-8c2f-6ed25553d336"||	"RISK00pg  Cholesterol:HDL Ratio > 5.5"|
  |"http://smartlifehealth.info/smh#feb4c246-2b4f-44b7-9b93-b172b2617b72"||	"RISK00ph  Cholesterol:HDL Ratio > 5.0"|
  |"http://smartlifehealth.info/smh#2d8929e6-aaea-48ce-bdf9-18248198cee3"||	"RISK00qd  Cholesterol:HDL Ratio > 5.0  Age < 53"|
  |"http://smartlifehealth.info/smh#eca4f5c0-cd94-459d-9c3c-9ed5af39b552"||	"RISK00pi  Cholesterol:HDL Ratio > 4.5"|
  |"http://smartlifehealth.info/smh#e5985685-e88d-467e-94cd-4a2efb4cd917"|                                                                      |	"RISK00pj  Cholesterol:HDL Ratio > 4.0"|
  |"http://smartlifehealth.info/smh#fd337aa6-d921-46bc-b344-ea57ca5bdb98"|         |	"RISK00qe  Cholesterol:HDL Ratio > 4.0  Age < 53"|
  |"http://smartlifehealth.info/smh#01fd4f0d-04db-4b69-a089-ebd35da40508"|                   |	"CRM  CKD Undiagnosed  CKD01Df  Patients with 2* eGFR<60 or 2*uACR>3"|
  |"http://smartlifehealth.info/smh#e4394821-cfb0-4a31-a622-deab74565d77"|       |	"CRM  DM QOF register  DM017 - Patients > = aged 17 on the diabetes register"|
  |"http://smartlifehealth.info/smh#d1672633-f4e1-4585-8c2b-35068cfdbdfd"||	"CRM  ETH00a  Not South Asian Ethnicity"|
  |"http://smartlifehealth.info/smh#cf8a97b3-3501-43bf-a6f4-70c13e225edf"||	"RISK00ea  Family history of heart disease < 60  Age < 60"|
  |"http://smartlifehealth.info/smh#20931287-109f-41b0-8a17-cc83b077cef6"||	"RISK00eb  Family history of heart disease < 60  Age < 53"|
  |"http://smartlifehealth.info/smh#49b380af-3cdc-41a8-9fe7-53583a1dcfff"||	"RISK00ec  Family history of heart disease < 60  Age < 45"|
  |"http://smartlifehealth.info/smh#acd2bd1d-cd79-40fc-9244-28f677de0ec5"||	"RISK00ed  Family history of heart disease < 60  Age < 38"|
  |"http://smartlifehealth.info/smh#b6eae552-a82c-47f0-b130-2cc269eec444"||	"RISK00ee  Family history of heart disease < 60  Age < 30"|
  |"http://smartlifehealth.info/smh#67cdd501-0b97-4c47-8576-fac54552ced5"||	"CRM  Mental health QOF register  MH001 - Patients on mental health register"|
  |"http://smartlifehealth.info/smh#4e8ac69a-743a-46a6-a6b5-e0fbfcb2454c"|                                               |	"RISK00ma  Heavy Smoker"|
  |"http://smartlifehealth.info/smh#10abe90b-4860-49c4-b59b-990f8ff58eb7"|         |	"RISK00mb  Heavy Smoker  Age < 65"                                                             |
  |"http://smartlifehealth.info/smh#84b53221-a4ff-4700-bbfc-29832731d763"||	"RISK00mc  Heavy Smoker  Age < 53"|
  |"http://smartlifehealth.info/smh#752e2034-7e94-49f4-974c-1af2dab51df1"||	"RISK00md  Heavy Smoker  Age < 38"    |
  |"http://smartlifehealth.info/smh#ab240773-c0c7-4f24-adae-e9c216ad3db3"|      |	"RISK00me  Heavy Smoker  Age < 30"                    |
  |"http://smartlifehealth.info/smh#4861721c-e27b-4caa-9b97-419ea3c2f4f2"|                  |	"RISK00na  Moderate or Unclassified Smoker"|
  |"http://smartlifehealth.info/smh#ebd80c6f-4501-4f83-a02a-f4c1bb9eba1d"|         |	"RISK00nb  Moderate or Unclassified Smoker  Age < 65"|
  |"http://smartlifehealth.info/smh#4058584c-7a12-4f0c-8151-fc7862e28668"||	"RISK00nc  Moderate or Unclassified Smoker  Age < 53"|
  |"http://smartlifehealth.info/smh#efe5120e-aea0-4ae6-8fcb-d4a6232ecdc2"||	"RISK00nd  Moderate or Unclassified Smoker  Age < 38"|
  |"http://smartlifehealth.info/smh#6f40c5d0-fbcf-4b39-896a-4aa5a5332130"||	"RISK00ne  Moderate or Unclassified Smoker  Age < 30"|
  |"http://smartlifehealth.info/smh#a0c2264d-f3e2-40c1-bf84-1fa48fca607a"||	"RISK00o  Light Smoker"|
  |"http://smartlifehealth.info/smh#bb7b2ae8-5b25-4a43-b7e2-afffb00e0153"||	"RISK00ia  Systemic lupus erythematosus"|
  |"http://smartlifehealth.info/smh#85bd73d6-9a7c-426e-8b21-e1f86a46cfb1"||	"RISK00ib  Systemic lupus erythematosus  Age < 45"|
  |"http://smartlifehealth.info/smh#375edecc-7a86-49bb-80e0-3b00fe96ed7e"|    |	"RISK00ic  Systemic lupus erythematosus  Age < 30"|
  |"http://smartlifehealth.info/smh#64853a13-351d-4ba1-a7b3-b1268963fd65"||	"CRM  CKD Undiagnosed  CKD01D  Patients who are likely to have CKD"                         |
  |"http://smartlifehealth.info/smh#190f490c-e3d2-4ae6-a5f4-f19bd72c17e7"|                                                           |	"RISK00da  Diabetes  Type 1 Diabetes"                                               |
  |"http://smartlifehealth.info/smh#0407bdb6-abcf-423b-906f-dd96c8f1a754"||	"RISK00db  Diabetes  Type 2 Diabetes"|
  |"http://smartlifehealth.info/smh#2d4211df-90b9-4874-ac49-b1c5f3dcf166"||	"RISK00dc  Diabetes  Age < 65"|
  |"http://smartlifehealth.info/smh#b7cae2d6-dace-47a3-ae53-d507cb912d90"||	"RISK00dd  Diabetes  Age < 60"|
  |"http://smartlifehealth.info/smh#e1759b59-fa2a-4d2f-a64f-a2a467ff111e"||	"RISK00de  Diabetes  Age < 55"|
  |"http://smartlifehealth.info/smh#cbba3080-4da5-4629-8003-ef46864958c1"||	"RISK00df  Diabetes  Age < 50"|
  |"http://smartlifehealth.info/smh#47c386de-a920-4243-bd42-acb326cf3847"||	"RISK00dg  Diabetes  Age < 45"|
  |"http://smartlifehealth.info/smh#a6dcc5c6-7dad-468c-b998-142fef5554c6"||	"RISK00dh  Diabetes  Age < 40"             |
  |"http://smartlifehealth.info/smh#16870d70-83c2-4f0a-a601-a6b6f962928e"||	"RISK00di  Diabetes  Age < 35"                    |
  |"http://smartlifehealth.info/smh#043445eb-5ec7-49d4-8199-84c168faacdc"|             |	"RISK00dj  Diabetes  Age < 30"    |
  |"http://smartlifehealth.info/smh#d9d53672-0c15-4732-a4fa-5773ef167537"|                 |	"RISK00sa  BMI > 50 or South Asian and BMI > 48"|
  |"http://smartlifehealth.info/smh#ff83460d-13cb-4a45-93c2-df76e509404f"||	"RISK00sb  BMI > 45 or South Asian and BMI > 43"|
  |"http://smartlifehealth.info/smh#7b83c4ee-7f5c-4061-8e19-5d8a1ab2c2be"||	"RISK00sc  BMI > 40 or South Asian and BMI > 38"|
  |"http://smartlifehealth.info/smh#ac7e7558-3c5b-43ee-9a33-5af11a7c34e3"||	"RISK00sd  BMI > 35 or South Asian and BMI > 33"                                |
  |"http://smartlifehealth.info/smh#969aa998-2668-4c0d-84c5-c2875ad00a8e"||	"RISK00se  BMI > 30 or South Asian and BMI > 28"           |
  |"http://smartlifehealth.info/smh#2a1ada31-7e4d-41ec-971c-809ab1ed5f9d"|                                                                    |	"RISK00k  Serious Mental Illness"                                                          |
  |"http://smartlifehealth.info/smh#ec60e7c7-4450-4f48-a2bf-96c12c158f9b"||	"CRM  CKD QOF register OR CKD undiagnosed  CKD005 or CKD01D"|
  |"http://smartlifehealth.info/smh#969b2681-9300-46c0-9f2f-4f366146d7ea"|                                                                   |	"RISK00fa  CKD  Age < 65"                                                              |
  |"http://smartlifehealth.info/smh#21a9e4e4-8d64-4844-97b3-94295f002278"|                                              |	"RISK00fb  CKD  Age < 58" |
  |"http://smartlifehealth.info/smh#cbb9705f-5b46-44de-b17c-a1eb6beb8f79"|                                      |	"RISK00fc  CKD  Age < 50"                                      |
  |"http://smartlifehealth.info/smh#e7133054-c85c-4325-b470-a0c911c1f0d5"|                                      |	"RISK00Ca  Low Risk  Option 3  0-9"|
  |"http://smartlifehealth.info/smh#fa35c368-48dc-41f0-a080-f0f23bf46780"|                      |	"RISK00d  No Risk  Option 3"         |
  |"http://smartlifehealth.info/smh#8678eda2-90ee-4628-9160-7596fce6dc0b"|                                                     |	"DL206f-Patients discussed at MDT MISSING Referral to Weight Management"|
  |"http://smartlifehealth.info/smh#43fd6c22-89d7-43e5-ab3f-e9c38e811fdb"|      |	"DL206g-Patients discussed at MDT MISSING Referral to ARRS Team"|
  |"http://smartlifehealth.info/smh#823a8354-80a9-44d6-9c80-921f76f9ae45"||	"NHS Numbers - DQ Report - MISSING Referral to Weight Mangement"|
  |"http://smartlifehealth.info/smh#982ffb07-9527-4d4c-8027-f4204406a701"||	"NHS Numbers - DQ Report - MISSING Referral to ARRS Teams"|
  |"http://smartlifehealth.info/smh#e9e3059e-cc3f-4130-aed3-f54fe9c2e954"|       |	"DL208f-Patients initiated/optimised on Insulin MISSING Referral to WM Progra (2)"|
  |"http://smartlifehealth.info/smh#43e28a4f-c326-4fd9-b19a-d2072ee6e646"||	"DL208g-Patients initiated/optimised on Insulin MISSING Referral to ARRS Teams"|
  |"http://smartlifehealth.info/smh#7acc1274-efba-4d81-a6e5-f7b9cb9fc285"||	"NHS Numbers - DQ Report - MISSING Referral to Weight Mangement"|
  |"http://smartlifehealth.info/smh#ec2211c8-3a57-4aa4-ab00-a86331346ba9"||	"NHS Numbers - DQ Report - MISSING Referral to ARRS Teams"|
  |"http://smartlifehealth.info/smh#3a36e8fd-55d4-484a-b285-1925d9251ce1"||	"DL209f-Patients initiated on GLP-1 MISSING Referral to Weight Management"|
  |"http://smartlifehealth.info/smh#15fb3491-614f-4bc1-a533-54d16e10cabf"||	"DL209g-Patients initiated on GLP-1 MISSING Referral to ARRS Team"|
  |"http://smartlifehealth.info/smh#9afce1b5-665b-467a-904b-5f37fc9de5c7"||	"NHS Numbers - DQ Report - MISSING Referral to Weight Mangement"|
  |"http://smartlifehealth.info/smh#d6ec2824-3ad9-4cac-a9ba-5587028c61ac"||	"NHS Numbers - DQ Report - MISSING Referral to ARRS Teams"|
  |"http://smartlifehealth.info/smh#f02df02c-ed8d-401c-9c30-f4aacd3ed536"|   |	"*MH00-ES-PAYMENT-Patients with Payable Follow Up AND ALL required MDS Completed"|
  |"http://smartlifehealth.info/smh#57fafc54-edd7-4463-baed-1b6abf43dd84"||	"MH00aa-ES-Patients with Annual Review AND ALL required MDS Completed"|
  |"http://smartlifehealth.info/smh#8e664053-c2c7-472c-a62c-edeae05b776c"||	"MH00ab-ES-Patients with Follow Up AND ALL required MDS Completed"|
  |"http://smartlifehealth.info/smh#48b45936-d050-4f67-8aa6-d2e85381a8d0"||	"*MH00-ES-PAYMENT-Patients with Payable Follow Up AND ALL required MDS Completed"|
  |"http://smartlifehealth.info/smh#114e4aa7-3522-4a06-813d-f70c5febbcc7"||	"MH00aa-ES-Patients with Annual Review AND ALL required MDS Completed" |
  |"http://smartlifehealth.info/smh#a7116d10-0468-4081-b311-f0738800b85f"||	"MH00ab-ES-Patients with Follow Up AND ALL required MDS Completed"|
  |"http://smartlifehealth.info/smh#581f87b2-427f-4540-a606-a1cca5a7b206"||	"Patients on Serious Mental Illness (SMI) Register (excluding those in remission)"|
  |"http://smartlifehealth.info/smh#7fa7528f-1d43-41de-ad13-4e715e2a32af"|                                                               |	"MH13b-ES-Patients with Lithium Monitoring recorded twice (in FY)"                               |
  |"http://smartlifehealth.info/smh#500eb555-2c5e-4d94-ab5b-557ba73a0a1e"||	"MH13a-Lithium treatment with prescription in financial year"  |
  |"http://smartlifehealth.info/smh#7059ae9a-70fa-4cf5-9473-a17611bca145"||	"MH13ba-ES-Patients with Serum Lithium recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#b2d64f90-4e02-4a29-9dc6-8dee8e798101"||	"MH13bb-ES-Patients with eGFR recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#f05fd07e-75b3-492f-a271-7b5d1c3aedb1"||	"MH13bc-ES-Patients with Serum TSH recorded twice (in FY)"|
  |"http://smartlifehealth.info/smh#c903e45e-1b80-4e11-862a-b20c5d0ed9bb"||	"SMI Register - Patients with SMI (MH001) deceased and deducted"|
  |"http://smartlifehealth.info/smh#0a6ee461-e9c4-4085-a4fb-7e09a739419d"||	"MH001 - Patients on the mental health register"|
  |"http://smartlifehealth.info/smh#b59a9153-92cd-47cc-aca5-3a65e718d91b"||	"SMI Register - Patients with SMI (MH001)"                                                        |
  |"http://smartlifehealth.info/smh#dc84bbb7-69f0-44ee-9863-cc64ea05e727"||	"MH08c-ES-Patients with Bowel Cancer Screening advise OR not eligible (in FY)"|
  |"http://smartlifehealth.info/smh#fb5dc13d-6f3f-49c8-a79a-09123ea40763"||	"MH08cb-NUM-Patients advised about Bowel Cancer Screening (in FY)"|
  |"http://smartlifehealth.info/smh#dee4c800-572d-42e2-9f68-5d2ade7a7a87"|   |	"MH08ca-ES-Patients aged 60-74"                                |
  |"http://smartlifehealth.info/smh#ba84e2c7-154b-4841-9dfc-30f39b125723"|                                          |	"MH08b-ES-Patients with Breast Cancer Screening advise OR not eligible (in FY)"|
  |"http://smartlifehealth.info/smh#b4c6b33b-f62a-4fb1-b191-95853341718e"||	"MH08bb-ES-Patients advised about Breast Cancer Screening (in FY)"                                |
  |"http://smartlifehealth.info/smh#437da96f-2a34-471c-bc54-71bf1e68b0ec"|           |	"MH08ba-ES-Female patient aged 50-70"                            |
  |"http://smartlifehealth.info/smh#8688a752-980f-42fe-84a3-90da3874f0a5"|                 |	"MH13-ES-Patients with Lithium monitoring recorded twice OR not on Lithium(in FY)"|
  |"http://smartlifehealth.info/smh#922498ab-9054-4806-8855-97b307af81ec"||	"Patients on Serious Mental Illness (SMI) Register (excluding those in remission)"|
  |"http://smartlifehealth.info/smh#1e5142fc-df64-460c-b1b6-65601589f235"|                                                                 |	"MH1.1 - Psychosis, schizophrenia or bipolar diagnosis (pts in remission)"      |
  |"http://smartlifehealth.info/smh#852b2cdb-5381-43f8-9114-232d0eaafc4f"||	"MH08-ES-Patients with appropriate Cancer Screening Prompts recorded (in FY)"|
  |"http://smartlifehealth.info/smh#f134b421-0d9c-49a5-ac15-85839d0bee20"||	"MH08a-ES-Patients with Cervical Cancer Screening advise OR not eligible (in FY)"|
  |"http://smartlifehealth.info/smh#8e679669-b707-4472-a55a-25bbe2fe24a6"||	"SMI Register - MDS Report More Detailed - NHS Numbers"|
  |"http://smartlifehealth.info/smh#a73608fa-635c-4dcf-845b-4377dbc5ce80"|          |	"SMI Register - MDS Report More Detailed - Anonymised Identifier"|
  |"http://smartlifehealth.info/smh#d71b135d-d861-4bb6-96de-23653376f2c9"||	"SMI Register - MDS Report - NHS Numbers"|
  |"http://smartlifehealth.info/smh#053502f2-2846-4108-a1c9-fc447bb7c31f"||	"SMI Register - MDS Report - Anonymised Identifier"|
  |"http://smartlifehealth.info/smh#8f8e8dbf-18b3-4f93-9333-3ce8a62b0575"|      |	"MH15-Follow Ups not recorded on same day as Annual Review (in FY)"                                                             |
  |"http://smartlifehealth.info/smh#73962bb7-898e-45ee-b8f2-093756f878f3"||	"MH15b-ES-Patients with Annual Review and Follow Up not recorded on the same day"    |
  |"http://smartlifehealth.info/smh#92aaf5f1-70ed-4604-99df-3072d0233820"||	"MH15b-Follow Ups recorded on same day as Annual Review (in FY) NOT PAYABLE"       |
  |"http://smartlifehealth.info/smh#aaa2543c-c539-4fed-8aa1-cfcc5c2303bd"|                                                            |	"MH15a-ES-Patients with Annual Review and Follow Up recorded on the same day"                                     |
  |"http://smartlifehealth.info/smh#63641d3d-141f-47ac-92d1-21c5e573488d"||	"MH15a-Total Follow Ups recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#1f2c13f2-c96e-4615-ab4e-98d637f83c55"||	"MH15-ES-Patients with Follow Up recorded (in FY)"             |
  |"http://smartlifehealth.info/smh#adff46ce-c7c2-40bb-8dd8-117bd5d96a70"|                       |	"MH14-Patients with Annual Review recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#9971655e-3670-4847-aa58-92af40396894"|         |	"MH14-ES-Patients with Annual Review (1st Appt) recorded (in FY)"|
  |"http://smartlifehealth.info/smh#9b640fbf-9b54-444f-985a-d79b93ad90cd"|         |	"MH13-Patients with Lithium Monitoring recorded twice OR not on Lithium"|
  |"http://smartlifehealth.info/smh#b0e5b3e6-2333-452e-bcdf-6fe86efa4cad"||	"MH13D-Patients on Lithium"                                     |
  |"http://smartlifehealth.info/smh#a3b04526-03ed-4dc1-b920-e5cc914d1464"||	"MH12-Patients with HbA1c/Blood Glucose recorded"|
  |"http://smartlifehealth.info/smh#d30ee372-1d5a-45b0-8695-a3509983b816"|                 |	"MH12-ES-Patients with HbA1c/Blood Glucose recorded"|
  |"http://smartlifehealth.info/smh#ecdae135-1269-49b9-88b8-516317426c7b"|       |	"MH12b-Patients with HbA1c/Blood Glucose NOT on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#c2cfd547-e8a1-47fa-878c-bc8b760ca7cc"||	"MH12b-ES-HbA1c/Blood Glucose NOT on Anti-Psychotics aged over 35 yrs(last 3yrs)"|
  |"http://smartlifehealth.info/smh#c1e58ef4-cbed-4600-8f20-80014baf54e6"||	"MH12a-Patients with HbA1c/Blood Glucose AND on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#ad1437a4-9dfe-4663-adcd-d57843061068"||	"MH12a-ES-Patients with HbA1c/Blood Glucose AND on Anti-Psychotics (in FY)"|
  |"http://smartlifehealth.info/smh#2b669008-d563-4b3f-a58b-89996d613af4"||	"MH11-Patients with Serum Cholesterol recorded"|
  |"http://smartlifehealth.info/smh#29b3c0a2-ea5a-4f82-9963-50c2ccc87779"||	"MH11-ES-Patients with Serum Cholesterol recorded"|
  |"http://smartlifehealth.info/smh#78124d3b-2a5b-44c8-9c07-4e593416f428"||	"MH11b-Patients with Serum Cholesterol NOT on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#4094b0d1-ba02-4745-9ce1-8704a155c2c1"||	"MH11c-ES-Patients aged under 35 yrs NOT on Anti Psychotics"|
  |"http://smartlifehealth.info/smh#daab321e-5eff-45da-b160-27f9e287628e"||	"MH11a-Patients with Serum Cholesterol AND on Anti Psychotics recorded"|
  |"http://smartlifehealth.info/smh#9919ba05-f86f-4eba-a796-bb294f04a9e4"||	"MH11a-ES-Patients with Serum Cholesterol AND on Anti Psychotics (in FY)"|
  |"http://smartlifehealth.info/smh#1a03a5f7-8ee9-4783-b909-05d9f2b1827c"||	"MH10-Patients with Medication Review recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#4edbf2a0-9aa3-4304-b2d3-cc89f050803d"||	"MH10-ES-Patients with Medication Review recorded (in FY)"|
  |"http://smartlifehealth.info/smh#d3fa8d7c-09ec-4ac8-a2e3-8072d477cbaa"||	"MH09e-Patients with Patient Goals recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#336ec208-97ed-4944-b63f-40a418d10eeb"||	"MH09e-ES-Patients with Patient Goals recorded (in FY)"     |
  |"http://smartlifehealth.info/smh#a8ce5bc2-7cc0-439d-be96-169a1a67e780"||	"MH09d-Patients with Health Action Plan recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#02a059e2-945e-49f2-aa12-bd3ffea86bcd"||	"MH09d-ES-Patients with Health Action Plan recorded (in FY)"|
  |"http://smartlifehealth.info/smh#3f88a67c-439a-465d-a261-4b968dcee385"||	"MH09c-Patients with Anticipatory Care Plan recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#ace910a0-4507-4306-8e4a-1fba76abee84"||	"MH09c-ES-Patients with Anticipatory Care Plan recorded (in FY)"|
  |"http://smartlifehealth.info/smh#6e023b15-fb6e-471c-8322-87d659a8f213"||	"MH09b-Patients with Signs Unwell recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#86190ed6-317b-43e3-99de-d9bef8ebbdc0"|                                                                 |	"MH09b-ES-Patients with Signs Unwell recorded (in FY)"|
  |"http://smartlifehealth.info/smh#9d2c36e4-1b8a-47d5-b32d-c24dbc4576cf"|         |	"MH09a-Patients with RaSWP recorded (in Financial Year)"                                                                  |
  |"http://smartlifehealth.info/smh#20b01aef-e906-4a39-b65a-d5ec06791900"||	"MH09a-ES-Patients with RaSWP recorded (in FY)"                |
  |"http://smartlifehealth.info/smh#b5f59108-ee8d-461d-a6ed-0f7ceae1a45f"|              |	"MH08cD-Patients eligible for Bowel Cancer Screening recorded"|
  |"http://smartlifehealth.info/smh#8396928e-78bc-4be0-af5b-b018efaf60cc"||	"MH08bD-Patients eligible for Breast Cancer Screening recorded"|
  |"http://smartlifehealth.info/smh#231b5ffa-aee8-4568-b07a-abe6c9f6041d"||	"MH08aD-Patients eligible for Cervical Cancer Screening recorded"|
  |"http://smartlifehealth.info/smh#0f6dccce-3ca1-4deb-a0dd-40d1e0ce99b6"||	"MH08aa-MDS-Female patient aged 25-64 with no history of hysterectomy"|
  |"http://smartlifehealth.info/smh#14f90e13-bd40-4c99-b24c-95717fa887c3"||	"MH07-Patients with Substance Misuse recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#28223d23-6d73-4107-9058-72ce3d03fa15"||	"MH07-ES-Patients with Substance Misuse recorded (in FY)"|
  |"http://smartlifehealth.info/smh#048bde75-8e70-4ffe-9923-288050d164cf"||	"MH06-Patients with Alcohol Intake recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#6ae5b9c8-3976-4f36-9d97-b22175057f88"||	"MH06-ES-Patients with Alcohol Intake recorded (in FY)"|
  |"http://smartlifehealth.info/smh#91024177-c807-432d-94b7-f4fd30951fe7"||	"MH05-Patients with Smoking Status recorded (in Financial Year)"|
  |"http://smartlifehealth.info/smh#171c71b0-e21b-4246-9865-926f2f1aada2"|     |	"MH05-ES-Patients with Smoking Status recorded (in FY)"|
  |"http://smartlifehealth.info/smh#dd8c8d5c-8051-45f9-9c37-76eacfe8ebcf"||	"MH02-ES-Patients with Blood pressure recorded (in FY)"|
  |"http://smartlifehealth.info/smh#d2d68863-d393-4846-99cd-577466ee5046"||	"MH02a-Blood Pressure reading excluding home done in Financial Year"|
  |"http://smartlifehealth.info/smh#03efc54e-0392-4331-bc69-d7d85a169455"||	"MH02b-Blood Pressure reading done at Home in Financial Year"|
  |"http://smartlifehealth.info/smh#cf9a525d-f6bd-488f-ab2f-7f1145f05f14"||	"MH08ab-ES-Patients advised about Cervical Cancer Screening (in FY)"|
  |"http://smartlifehealth.info/smh#6fc669c8-6a8c-4a18-8a20-61813582f1bc"||	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis"|
  |"http://smartlifehealth.info/smh#c4909e02-88d3-4480-8a93-655fdd093dec"||	"MH2_REG - Lithium treatment with prescription in last 6 months"|
  |"http://smartlifehealth.info/smh#fdedffcf-4097-4422-972f-49f381783823"||	"MH11b-ES-Serum Cholesterol NOT on Anti Psychotics aged over 35 yrs(in last 3yrs)"|
  |"http://smartlifehealth.info/smh#5a24af18-219a-4853-94a4-e0a341fdadd8"||	"MH12c-ES-Patients aged under 3yrs NOT on Anti-Psychotics (last 3yrs)"                                                             |
  |"http://smartlifehealth.info/smh#20e86501-cc8f-4c36-a203-3d63f8204ec0"||	"MH01-ES-Patients with BMI recorded (in FY)"                              |
  |"http://smartlifehealth.info/smh#6d3b8eec-780d-4220-94ab-05bde548ebb9"||	"MH03-ES-Patients with Diet Status recorded (in FY)"              |
  |"http://smartlifehealth.info/smh#46e95353-c2b0-4c6e-ba66-950d71c45a6b"||	"MH04-ES-Patients with Exercise Assessment recorded (in FY)"  |
  |"http://smartlifehealth.info/smh#2b665030-dc11-458a-8efd-009411a5a44a"||	"MH04-Patients with Exercise Assessment recorded (in Financial Year)"  |
  |"http://smartlifehealth.info/smh#5a269098-b4ae-46e7-889b-6e08cc3ef140"||	"MH03-Patients with Diet Status recorded (in Financial Year)"       |
  |"http://smartlifehealth.info/smh#5e4c517c-9429-4883-aff5-d567ceffdf5a"||	"MH02-Patients with Blood Pressure recorded (in Financial Year)"     |
  |"http://smartlifehealth.info/smh#bf07e884-93be-4bbd-936f-ea44bb799367"|  |	"MH01-Patients with BMI recorded (in Financial Year)"                |
  |"http://smartlifehealth.info/smh#eb4bf753-1fe4-4cd6-8e31-843d6073224a"||	"MH08-Patients with appropriate Cancer Screening recorded"                                            |
  |"http://smartlifehealth.info/smh#b5d5ee6b-c1ac-46f0-a34a-ece2a2e38bd7"||	"MH13N-Patients with Lithium Monitoring recorded twice (in Financial Year)"      |
  |"http://smartlifehealth.info/smh#03246d4e-0a10-43f3-94df-4349ebf3fe9a"||	"MH13Nc-Patients with Serum TSH recorded twice (in Financial Year)"  |
  |"http://smartlifehealth.info/smh#026770da-920e-459f-a1f4-571eb4b970e0"| |	"MH13Nb-Patients with eGFR recorded twice (in Financial Year)"                 |
  |"http://smartlifehealth.info/smh#7856676a-e244-40aa-8af2-3085affca14e"| |	"MH13Na-Patients with Serum Lithium recorded twice (in Financial Year)"                                                      |
  |"http://smartlifehealth.info/smh#d9d99056-ebcd-4aca-a570-3125d05b9cb5"||	"MH08cN-Patients advised about Bowel Cancer Screening advice (in FY)"|
  |"http://smartlifehealth.info/smh#e72cc7b4-a59e-47cb-9dfb-35233eb91dbb"||	"MH08bN-Patients advised about Breast Cancer Screening advice (in FY)"|
  |"http://smartlifehealth.info/smh#a34bb208-3380-46b4-9107-81a0918148e1"||	"MH08aN-Patients advised about Cervical Cancer Screening advice (in FY)"|
  |"http://smartlifehealth.info/smh#f53f5765-a24f-4a85-afa5-e6c70343c2e4"|                                                              |	"MH00-ES-Patients with MH01-13 Completed"                                    |
  |"http://smartlifehealth.info/smh#1730c02d-8996-4004-ba3b-c6fbeb3a0cb3"|                |	"MH00ab-ES-Patients with Follow Up AND ALL required MDS Completed" |
  |"http://smartlifehealth.info/smh#ec485723-82f4-4352-bd68-3f448e058088"||	"MH00aa-ES-Patients with Annual Review AND ALL required MDS Completed"|
  |"http://smartlifehealth.info/smh#ac993ce4-0643-462e-9512-571c671b43a8"||	"*MH00-ES-PAYMENT-Patients with Payable Follow Up AND ALL required MDS Completed"|
  |"http://smartlifehealth.info/smh#a1d42ac1-5f14-4b79-b4f1-3d8e543b52bb"|                                                               |	"MH2_REG - Lithium treatment with prescription in last 6 months"                                                             |
  |"http://smartlifehealth.info/smh#c861af73-5df7-4b3d-a504-f61df7ceb808"||	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis"            |
  |"http://smartlifehealth.info/smh#65df0e43-f9ea-4cd9-acb5-96e1d336f3d9"|  |	"MH2_REG - Lithium treatment with prescription in last 6 months"                              |
  |"http://smartlifehealth.info/smh#f3874c2f-f276-41a5-92ca-731498e460de"||	"MH1_REG - Psychosis, schizophrenia or bipolar diagnosis" |
  |"http://smartlifehealth.info/smh#f029fed2-dde4-4a91-b4f3-36cf02f3a659"||	"MH001 - Patients on the mental health register"       |
  |"http://smartlifehealth.info/smh#9b69ac38-1d45-42f8-984b-a4707eacfaca"| |	"MH001 - Patients on the mental health register"         |
  |"http://smartlifehealth.info/smh#36f10896-0d0f-4d59-880b-d14b8d703991"|                          |	"Upload FULL NWL SMI 1.5-CP v2.0.250425 (Latest1) -report"|
  |"http://smartlifehealth.info/smh#cfc7e496-8a4b-4cf1-9bdd-09f364bf6a92"||	"Upload 2of2 NWL SMI 1.5-CP v2.0.250425 (Latest1) -report"|
  |"http://smartlifehealth.info/smh#8d5d0836-e9e5-47a3-b536-0a63821166c8"|               |	"Mental Health Dashboard -report"               |
  |"http://smartlifehealth.info/smh#d62f4804-7248-489d-8eb9-211ec3413a3a"|                                    |	"CRM10c  DQ  LAST 15M TO END OF FY  Latest Non HDL Chol > 3"|
  |"http://smartlifehealth.info/smh#32942ae4-d1d7-4c04-9795-062f2c2d3c83"|     |	"CRM10a  DQ  LAST 15M TO END OF FY  Latest HbA1c > target"                               |
  |"http://smartlifehealth.info/smh#471f6283-aa02-4277-910e-3df0907b2f86"|               |	"CRM10b  DQ  LAST 15M TO END OF FY  Latest BP > target"   |
  |"http://smartlifehealth.info/smh#7550b614-5c6c-42f5-9fed-587e77e97568"|                           |	"*CRM10  DQ  LAST 15M TO END OF FY  3 Treatment Targets NOT Completed"|
  |"http://smartlifehealth.info/smh#3fa49437-872e-4c40-921e-707bdc96d72e"| |	"Patients with Moderate or Severe Frailty or aged >= 80"|
  |"http://smartlifehealth.info/smh#d2247d72-21c0-4f68-8d8f-04771db2fb55"||	"Patients with no Moderate or Severe Frailty or aged < 80"|
  |"http://smartlifehealth.info/smh#bf4bf348-d6bf-48fd-8fa1-2ad505f55d5a"||	"NHS NUMBERS  DQ  3 Treatment Targets NOT Achieved -report"|
  |"http://smartlifehealth.info/smh#d50db7c9-06b4-41eb-880b-60ac29f5440b"||	"NHS NUMBERS  DQ  3 Treatment Targets NOT Achieved (More Detailed) -report"|
  |"http://smartlifehealth.info/smh#52158b9b-70b3-4c6c-a1da-57e50d467dde"||	"NHS NUMBERS  DQ  3 Treatment Targets NOT Achieved -report"|
  |"http://smartlifehealth.info/smh#373f381e-bc23-45e5-9229-ce86a1371018"||	"NHS NUMBERS  DQ  3 Treatment Targets NOT Achieved(More Detailed) -report"|
  |"http://smartlifehealth.info/smh#552ff2b1-46c0-49de-86bd-b3ac7ca6b73c"||	"CRM11  DQ  LAST 15M TO END OF FY  Diagnosed in last 2 yrs HbA1c > 48"                                                             |
  |"http://smartlifehealth.info/smh#ee351f08-47d3-43af-b999-ecb7df859994"|                                                        |	"NHS NUMBERS  Diagnosed in last 2 yrs without HbA1c <= 48 -report"                                                               |
  |"http://smartlifehealth.info/smh#bd1c2d23-549c-48aa-b4dd-7603dedf974d"||	"CRM12  DQ  THIS FY  Black & Black Britsh Hypertensive WITHOUT BP <140/90"|
  |"http://smartlifehealth.info/smh#7fb861f0-3a4a-48aa-9153-d450cb8eb740"||	"NHS NUMBERS  DQ  Black & Black British Patients MISSING latest BP <140/90 -report"|
  |"http://smartlifehealth.info/smh#eb30bfaa-8849-4c00-9cfc-345a5339a7c6"||	"CRM00  CRM Patients  LAST 15M TO END OF FY  First appointment recorded"|
  |"http://smartlifehealth.info/smh#be4d9d3e-9f4e-4b50-929d-58ce48bd4b4d"||	"RISK00A  Group 1  Care Plan  14 or more risk factors"|
  |"http://smartlifehealth.info/smh#832d6f87-a819-4d6c-ac9b-13dfdf27167d"||	"RISK00Ca  Group 3  Care Plan  1 to 9 risk factors"|
  |"http://smartlifehealth.info/smh#1a4be09f-94af-4730-8c41-dbb0a0c9ef8b"||	"RISK00B  Group 2  Care Plan  10 to 13 risk factors"|
  |"http://smartlifehealth.info/smh#e5fcbe41-193d-4808-b303-9f97a2272fe2"||	"RISK00Cb  Group 3  Care Plan  0 Risk Factors"|
  |"http://smartlifehealth.info/smh#881109a2-5fa4-4b67-baf0-7a407a20ea5b"||	"RISK00C  Group 3  Care Plan  0 to 9 risk factors"|
  |"http://smartlifehealth.info/smh#789f60a9-2ef0-458b-a663-1c3089002624"||	"CRM00  CRM Patients  LAST 15M TO END OF FY  First appointment NOT recorded"|
  |"http://smartlifehealth.info/smh#158a0eee-1f53-4515-ae28-e0998c8f76f2"||	"RISK00A  Group 1  No Care Plan  14 or more risk factors"|
  |"http://smartlifehealth.info/smh#c36552db-cdf6-4b65-aeff-3b94aa8dd4be"||	"RISK00Ca  Group 3  No Care Plan  1 to 9 risk factors"|
  |"http://smartlifehealth.info/smh#2fe65936-c3f6-4fe3-82f1-605acb2eb095"||	"RISK00B  Group 2  No Care Plan  10 to 13 risk factors"|
  |"http://smartlifehealth.info/smh#4a27d6e4-87f8-4d37-adfa-e5726c168012"||	"RISK00Cb  Group 3  No Care Plan  0 Risk Factors"|
  |"http://smartlifehealth.info/smh#128571bd-2be3-4d55-b70a-3ba78fffd7eb"||	"RISK00C  Group 3  No Care Plan  0 to 9 risk factors"|
  |"http://smartlifehealth.info/smh#89e756d6-cede-4982-9052-6d6f02429d7b"||	"CRM10c  ACHIEVED  LAST 15M TO END OF FY  Latest Non HDL Chol <=3"|
  |"http://smartlifehealth.info/smh#52a51b74-d114-43da-b483-db175a7ac4ab"||	"CRM10a  ACHIEVED  LAST 15M TO END OF FY  Latest HbA1c <= appropriate target"|
  |"http://smartlifehealth.info/smh#027f03bd-ff74-411a-a6f0-b0a47aeda607"||	"CRM10b  ACHIEVED  LAST 15M TO END OF FY  Latest BP <= appropriate target"|
  |"http://smartlifehealth.info/smh#4a1294c8-d5ea-4b5a-9f93-f962fd303c06"||	"*CRM10  ACHIEVED  LAST 15M TO END OF FY  3 Treatment Targets"|
  |"http://smartlifehealth.info/smh#703fb556-3fe4-4d30-9b47-2c0f76055ffa"||	"CRM11  ACHIEVED  LAST 15M TO END OF FY  Diagnosed in last 2 yrs HbA1c<=48"|
