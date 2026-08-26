Feature: IMQ to SQL conversion
  @IMQClinicalIndicatorsTest
  Scenario Outline: IMQ converts to SQL without errors
    When IMQ to SQL conversion is executed for <iri>
    Then SQL should be generated successfully

  Examples:
  | iri                                                            | count | label                                                                                                |
  |----------------------------------------------------------------|-------|------------------------------------------------------------------------------------------------------|
  |http://endhealth.info/qof#36e70531-462a-427a-8ee2-eeb584018cc9|  |	AST_REG - Patients on the asthma register|
  |http://endhealth.info/qof#c6395829-91f1-413a-af70-f2377785e80f|  |	AST015 - Asthma with review in last 12 months|
  |http://endhealth.info/qof#9f398415-29b4-4823-9a4c-598cec5f52e9|  |	AST014 - Asthma with objective tests recorded|
  |http://endhealth.info/qof#da52a278-b035-4df3-986a-747403198172|  |	AFIB_REG - Patients on the AF register|
  |http://endhealth.info/qof#af79dc89-9e7e-4ccd-999a-4291972982d5|  |	AF006 - Patients with AF and CHA2DS2-VASc score|
  |http://endhealth.info/qof#68e2c3b5-5315-4b72-9d71-1a1e21be51a0|  |	AF008 - Patients with AF and a CHA2DS2-VASc score>=2 on DOAC /Vit K antagonist|
  |http://endhealth.info/qof#10cff25e-adb1-4b76-9129-bc819ba5aa70|  |	CD_REG - Patients on Cardiovascular disease register|
  |http://endhealth.info/qof#d453fa6c-8b7b-418f-80d0-789e303f13e8|  |	CD001 - CHD / stroke / TIA age <=79 and BP <=140/90|
  |http://endhealth.info/qof#ae2af3e5-d9ba-470f-b274-c4b275037d79|  |	CD002 - CHD / stroke / TIA age >=80 and BP <=150/90|
  |http://endhealth.info/qof#b4ab3ade-3814-483c-8f70-153be3db1bc4|  |	CHD_REG - Patients on the CHD register|
  |http://endhealth.info/qof#296bcdcd-cdb3-4dd0-ab30-62fc0cd8a361|  |	CHD005 - CHD taking anti-platelet or anti-coagulant|
  |http://endhealth.info/qof#22edf68c-978d-415d-bb29-c15e7d247ea0|  |	COPD_REG - Patients on the COPD register|
  |http://endhealth.info/qof#9122a814-3f8a-4c97-a197-4c0598de7dd6|  |	COPD010 - COPD with review including exacerbations, breathlessness|
  |http://endhealth.info/qof#fc97829e-7cff-4025-bf9a-139ce99753e9|  |	DEM_REG - Patients on the dementia register|
  |http://endhealth.info/qof#e523c16f-ec8c-4acb-830d-cb3f9ed7d2dc|  |	DEM004 - Dementia and review done|
  |http://endhealth.info/qof#3664cefb-aa73-4866-b8ac-930fd2fc0251|  |	DM_REG - Patients aged 17 or over on the diabetes register|
  |http://endhealth.info/qof#64cfb237-3b41-4339-ab0b-b5253d51681d|  |	DM006 - Diabetes and proteinuria or micro-albuminuria taking ACE-I or ARB|
  |http://endhealth.info/qof#c07dbb19-b448-43a6-9052-946b2978c1fa|  |	DM014 - Newly diagnosed with diabetes referred to structured education programme|
  |http://endhealth.info/qof#ae1b3bfe-8004-40c4-92f3-21c001d0064a|  |	DM036 - Diabetes no moderate/severe frailty BP 140/90 mmHg or less|
  |http://endhealth.info/qof#d9249cfd-4a50-4390-a7df-35ebbdbfba5f|  |	DM020 - Diabetes no moderate/severe frailty IFCC-HbA1c is 58 mmol/mol or less|
  |http://endhealth.info/qof#2cf8c038-2e3f-497d-af42-2509b289fe7f|  |	DM021 - Diabetes with frailty IFCC-HbA1c is 75 mmol/mol or less|
  |http://endhealth.info/qof#49c7bac7-a262-4ae6-8c1b-3fb038817cea|  |	DM034 - Diabetes aged >=40 no CVD/frailty on statin or lipid-lowering therapy|
  |http://endhealth.info/qof#fd3a4119-5a23-4c0c-b9e6-5b77dad9d0a1|  |	DM035 - Diabetes with CVD (not haemorrhagic) on statin or lipid-lowering therapy|
  |http://endhealth.info/qof#22ce080f-9892-4509-8521-ec0ca0b85360|  |	DM037 - Diabetes with care processes performed in last 12 months|
  |http://endhealth.info/qof#35678b18-dcc1-45ff-bdc8-3b66400ba491|  |	HF1 Register - Patients with an unresolved diagnosis of heart failure|
  |http://endhealth.info/qof#58b232f3-ad50-4ccc-b423-7888f770beb4|  |	HF007 - Heart failure with review of functional capacity, medication|
  |http://endhealth.info/qof#acafa8c6-3fae-494e-8a6c-a0a02ca61af0|  |	HF3 Register - Unresolved heart failure due to reduced EF|
  |http://endhealth.info/qof#7b3d1578-4696-44ea-b86d-d9ecb2a9e8dd|  |	HF008 - Heart failure diagnosis from 1 April 2023 with ECHO or specialist assessment|
  |http://endhealth.info/qof#debc2cfb-f85e-4b52-8e0d-90647e02a877|  |	HF009 - HF patients on ACE-I/ARB/ARNI, beta-blocker, MRA and SGLT2 inhibitor|
  |http://endhealth.info/qof#e6e5d441-77d5-4520-af34-ec209d62bccb|  |	HYP_REG - Patients on the hypertension register|
  |http://endhealth.info/qof#aec4b841-2cff-4df5-b16b-ee61ed8c415b|  |	HYP010 - Hypertension age <=79 no moderate/severe frailty, BP<=140/90|
  |http://endhealth.info/qof#3c10e30e-7316-4eb3-9dd4-5c4079f31462|  |	HYP011 - Hypertension age >=80 no moderate/severe frailty, BP<=150/90|
  |http://endhealth.info/qof#7d57f834-3595-4834-ac29-4a2a33092339|  |	MH1_REG - Psychosis, schizophrenia or bipolar diagnosis|
  |http://endhealth.info/qof#754ac557-5ae8-4a72-8a70-f16df3eec811|  |	MH002 - Mental health and care plan documented|
  |http://endhealth.info/qof#2814f6c2-d47b-4721-b6be-7bb705f7c304|  |	MH003 - Mental health problems and BP recorded in the preceding 12 months|
  |http://endhealth.info/qof#2d4c8da0-06f9-4a67-81cf-b2de99a12642|  |	MH006 - Mental health problems and BMI recorded in the preceding 12 months|
  |http://endhealth.info/qof#6fe140ea-91ba-478b-9549-8053ff634d80|  |	MH011 - Mental health problems and lipids recorded in last 12 months|
  |http://endhealth.info/qof#060dcebf-761d-48cc-8224-44549cb4062f|  |	MH007 - Mental health problems and alcohol recorded in last 12 months|
  |http://endhealth.info/qof#62d9cdd1-f120-49f5-ac08-4f52e14943a9|  |	MH012 - Mental health problems and glucose/Hba1c recorded in last 12 months|
  |http://endhealth.info/qof#7f4e269a-7ac6-4eeb-b39c-0d5a59f6c576|  |	STIA_REG - Patients on the stroke or TIA register|
  |http://endhealth.info/qof#286416f9-56ea-4764-b34c-9c50014a9cf5|  |	STIA007 - Non-haemorrhagic stroke or TIA taking anti-platelet or anti-coagulant|
  |http://endhealth.info/qof#692cf9df-6c19-4f92-bc65-5048290438e5|  |	Patients on NDH register (IMPORTANT: no results until update)|
  |http://endhealth.info/qof#5a588247-94e8-4c1c-889b-b8bc7acbf7eb|  |	VI001 - Three or more DTaP vaccine doses before 8 months|
  |http://endhealth.info/qof#a75b9c8f-15f3-460d-a75a-da7c3ef16b0c|  |	VI002 - One or more MMR or MMRV doses aged 12-18 months|
  |http://endhealth.info/qof#eeacfb90-6eda-466e-b3c4-daa985e0acfb|  |	VI003 - Aged 1-5 years with DTaP/IPV booster and at least 2 doses of MMR or MMRV|
  |http://endhealth.info/qof#f12a398d-0795-4ae5-aa12-507c3284589c|  |	VI004 - Aged 80 with shingles vaccine received between 70-79 years|
  |http://endhealth.info/qof#6012a8ca-f822-479d-b0c4-4aedacd8792d|  |	CHOL2REG - Patients with CHD / PAD / TIA|
  |http://endhealth.info/qof#44293066-0064-4eb2-9d94-cbc3a42df8a5|  |	CHOLREG - Patients with CHD / PAD / TIA or >=18 with CKD G3a to G5|
  |http://endhealth.info/qof#2185db11-16a8-4ceb-be77-945063438372|  |	CHOL003 - Patients with CHD / PAD / TIA / CKD on statin/lipid-lowering therapy|
  |http://endhealth.info/qof#6fad2b05-d9ba-4a71-9f47-7f978999afad|  |	CHOL004 - Patients with LDL cholesterol <=2.0 or non-HDL <=2.6|
  |http://endhealth.info/qof#38e66f24-556c-4453-94b8-524608a2e69b|  |	EPIL_REG - Patients on the epilepsy register|
  |http://endhealth.info/qof#12b62913-0b06-45b7-b20b-bc91049d3b65|  |	CAN_REG - Patients on the cancer register|
  |http://endhealth.info/qof#4c6c72be-4171-4a25-b361-a2121ce42be5|  |	PALCARE_REG - Patients on the palliative care register|
  |http://endhealth.info/qof#89acf3b9-1eda-413b-85f9-dfc8bf3c575f|  |	DEP1_REG - Patients on the depression register|
  |http://endhealth.info/qof#739e4c11-559e-4bb9-ae1e-652ed98dab69|  |	LD5_REG - Patients on the learning disabilities register|
  |http://endhealth.info/qof#a9111729-52ef-4f56-bc64-8cbe019102ef|  |	PAD_REG - Patients on the peripheral arterial disease register|
  |http://endhealth.info/qof#1adc36e3-980a-44ef-9e0c-9ea33368b7a3|  |	OSTEO2_REG - Age >=75 on the osteoporosis register|
  |http://endhealth.info/qof#10033d27-52d5-4a00-a369-da2d5f4369b7|  |	RA_REG - Patients on the rheumatoid arthritis register|
  |http://endhealth.info/qof#7ec3aa7a-f5af-4fb9-b67b-4b7b61110621|  |	CKD_REG - Patients on the CKD register|
  |http://endhealth.info/qof#4ffbd9f1-ed87-46a6-a277-817a9a115ea1|  |	BP002 - Patients aged 45 or over and blood pressure recorded|
  |http://endhealth.info/qof#9de17b0d-16d6-4cc0-8e71-d99216c54de4|  |	CS005 - Aged 25-49 adequate smear last 3 years and 6 months|
  |http://endhealth.info/qof#8007a4bf-9603-4214-a5aa-a0cee37e7854|  |	CS006 - Aged 50-64 adequate smear last 5 years and 6 months|
  |http://endhealth.info/qof#a4d2d322-041d-4484-9fc2-eddd55a6eaf8|  |	OBES_REG - Patients age >=18 on obesity register|
  |http://endhealth.info/qof#5d7112d7-8203-4878-b7e5-2de8bd3a5e93|  |	OBES2_REG - Patients age >=18 on obesity register with 4+ diagnoses|
  |http://endhealth.info/qof#1ae45c43-fc6c-47ae-b539-489c635ec9c9|  |	OB004 - Patients age >=18 with obesity referred to weight management|
  |http://endhealth.info/qof#6db45cf4-6bf5-48a6-9e28-40c3c6203da9|  |	OB005 - Patients NICE TA1026 cohort, shared decision, meds, behavioural referral|
  |http://endhealth.info/qof#3d88a9e9-9f41-48a0-bfa3-3d54d53499cb|  |	SMOK2_REG: Patients aged 15 years and over for Smoking|
  |http://endhealth.info/qof#9a8fd4c1-cc24-411b-bccc-a53a244069e4|  |	SMOK004 - Current smokers with an offer of support given|
  |http://endhealth.info/qof#f34d2ecd-3c8b-4367-9936-ec564a1cb9b7|  |	SMOK1_REG: Patients with Chronic Diseases|
  |http://endhealth.info/qof#6709086c-8d6b-4f71-8992-ff69bd4e0953|  |	SMOK002 - Chronic disease and smoking status recorded|
  |http://endhealth.info/qof#7ADE6421-147B-4F6C-9139-EF569EF9E1AC|  |	LTC Manager - A list of LTC patients due annual review in QOF 26-27|
  |http://endhealth.info/qof#2f4467fb-46ae-4866-9380-904ddf158fec|  |	LTC Patients without Invitation codes within last 6 weeks|
  |http://endhealth.info/qof#c8d3ca80-ba23-418b-8cef-e5afac42764e|  |	Stroke register|
  |http://endhealth.info/qof#e2d08005-1058-40bb-b1e4-1b3c7961da7f|  |	[AST015] - Asthma eligible for review|
  |http://endhealth.info/qof#71f37802-5da2-4988-8673-bcebbc5dced8|  |	[AST014] - Asthma eligible for objective tests|
  |http://endhealth.info/qof#8f173efa-d577-449f-a1fd-ee8623de2107|  |	Patient selected in Rule 2 of [AST014]|
  |http://endhealth.info/qof#b955ede9-4ed2-41e2-94bf-79eae077d777|  |	Patient selected in Rule 3 of [AST014]|
  |http://endhealth.info/qof#1b8b6652-f642-4d9c-9dab-56dbc96c0508|  |	Patients selected for unresolved asthma before registered in the last 12 months|
  |http://endhealth.info/qof#ca6d3291-6619-4f87-9128-e09091e31a38|  |	Patients who have Spirometry or FeNo or BDR test in the last 93 days|
  |http://endhealth.info/qof#ada66482-16d3-4fb7-92d3-6869281086eb|  |	Patient had peak expiratory flow rate (PEFR) variability test in last 93 days|
  |http://endhealth.info/qof#0e614be1-3193-4c83-b46b-1d8da6fd36d3|  |	Patient had total IgE and eosinophil test in last 93 days|
  |http://endhealth.info/qof#40798e27-e7f9-4885-985c-fb5b4f29967e|  |	Patients on asthma register age >= 20|
  |http://endhealth.info/qof#90f6f1c5-61cb-4d31-970e-4cecfdab3011|  |	Patients on the asthma register who don't have a latest code of resolved asthma|
  |http://endhealth.info/qof#2221fcf3-b20f-4d4c-92ea-7974eefcfa16|  |	[AF006] - Patients with AF eligible for a stroke risk assessment|
  |http://endhealth.info/qof#3a38265f-1214-4481-986e-697330cca839|  |	[AF008] - Patients with AF and a CHA2DS2-VASc score>=2 on DOAC /Vit K antagonist|
  |http://endhealth.info/qof#1dd66c6b-3f1f-44c9-a088-207e2b4398f9|  |	No valve on vit K antag + DOAC declined/not indicated or APL contraindicated|
  |http://endhealth.info/qof#b0a4a753-7b26-4310-88fd-3c4e56639e49|  |	Mechanical prosthetic valve on vit K antagonist within 6m before QOF year end|
  |http://endhealth.info/qof#b7addbf5-34b4-4379-a3ff-415765b6b4aa|  |	DOAC not indicated in QOF year or APL/DOAC contraindicated|
  |http://endhealth.info/qof#0cacc398-6624-46bd-b941-f717009bcc17|  |	AF Register in the 3 month period|
  |http://endhealth.info/qof#c347a250-8609-44d6-8039-366eaf672518|  |	DOAC not indicated within 1y and TTR >=65% within 6 mths before month end|
  |http://endhealth.info/qof#0ee297a7-a8ac-4ace-94f1-7fe2b8763edb|  |	DOAC declined/not indicated (TTR>=65% in 6m) in mth end or APL contraindicated|
  |http://endhealth.info/qof#a4d509f5-0d63-4409-aef9-9523cf485c86|  |	[CD001] - CHD or Stroke or TIA aged 79 years or under eligible for BP|
  |http://endhealth.info/qof#ea44c692-bf35-42d8-9d3d-e105b030c361|  |	[CD002] - CHD or stroke or TIA aged 80 years or over eligible for BP|
  |http://endhealth.info/qof#7ba9d980-9ca2-48d4-bc12-5e4cb8f862f8|  |	Patients with Stroke/TIA or CHD and their quality care is deemed as unsuitable|
  |http://endhealth.info/qof#b82688c5-e09f-48b4-b529-cb0059be4ce3|  |	Patients with Stroke/TIA and declined quality indicator care|
  |http://endhealth.info/qof#146f1076-b697-4e97-a15a-469f421b75f5|  |	Age <80y latest BP above target in QOF year and 2 stroke/CHD invitations|
  |http://endhealth.info/qof#6c1192ef-8c7c-4918-bec0-46406fe0da92|  |	No BP recorded; 2 invites for stroke/TIA or CHD monitoring given|
  |http://endhealth.info/qof#0036e203-6cf9-43cc-acad-4a1cc6048d38|  |	Age >=80y latest BP above target in QOF year and 2 stroke/CHD invitations|
  |http://endhealth.info/qof#8bbf6877-6a3f-4955-82a7-c42e95ddac33|  |	Latest BP in QOF year and 2 CHD invitations|
  |http://endhealth.info/qof#40749121-5ad0-4dfc-981a-612fb918530e|  |	Latest BP in QOF year and 2 stroke invitations|
  |http://endhealth.info/qof#db55beb1-3203-4971-8961-8a1b2a085d20|  |	Latest BP in QOF year and 2 stroke/CHD invitations|
  |http://endhealth.info/qof#4accdcc4-54bf-4687-a2ff-aee80366d404|  |	Age <80y latest BP above target in QOF year|
  |http://endhealth.info/qof#9c2fe122-e857-4b4e-b73d-e0e6f5b91149|  |	Age >=80y latest BP above target in QOF year|
  |http://endhealth.info/qof#a9c1ea3b-3fc0-492b-acc1-63f488c778cd|  |	No BP recorded in last 12 months and 2 CHD invitations|
  |http://endhealth.info/qof#45a135f8-5f27-4992-9dc8-36d51b51278d|  |	No BP recorded in last 12 months and 2 stroke invitations|
  |http://endhealth.info/qof#773600bd-3726-4f73-9483-798023d5b83f|  |	[CHD005] - CHD and eligible for aspirin, anti-platelet or anti-coagulant|
  |http://endhealth.info/qof#246a09cb-3384-435b-be70-016c93b74b89|  |	Patients Registered in the 3 month period|
  |http://endhealth.info/qof#eeaf9cc0-eec8-4853-9a16-9ff589021143|  |	Patients Registered in the 9 month period|
  |http://endhealth.info/qof#7804aa4d-cdb9-4781-bc67-78886130c054|  |	No BP recorded in last 12 months and 2 earliest CHD invitation|
  |http://endhealth.info/qof#ea40a581-bf0d-412b-9453-e07d87d64f63|  |	[COPD010] - COPD and eligible for review including exacerbations, breathlessness|
  |http://endhealth.info/qof#8d5a2661-02ea-45d8-97d6-ba3c85b98ec4|  |	Earliest unresolved COPD diagnosis|
  |http://endhealth.info/qof#e1ddce33-ea75-46f5-9cf2-7f8cdece58ed|  |	Unresolved COPD + FEV1/FVC ratio <0.7 3-6m post diagnosis|
  |http://endhealth.info/qof#af609321-626b-4df0-8334-79b12f868c4a|  |	Patient who didn't have resolved COPD diagnosis|
  |http://endhealth.info/qof#332cee6b-0c95-4486-b125-dab3c935a876|  |	Patients with unresolved COPD diagnosis|
  |http://endhealth.info/qof#7e0ce415-e4ab-471e-872e-bfd15e4ba6cc|  |	[DEM004] - Dementia and review done|
  |http://endhealth.info/qof#efab57f8-cd55-46a8-8013-9ff398f38c6f|  |	[DM006] - Proteinuria or microalbuminuria eligible for ACE-I or ARB|
  |http://endhealth.info/qof#838e772c-43c0-4f18-8c39-d67433a0ae91|  |	[DM014] - Newly diagnosed diabetes and eligible for a referral to a structured education programme|
  |http://endhealth.info/qof#de449e98-d90c-4610-87a5-87dc0476a9a7|  |	[DM036] - Diabetes and no moderate/severe frailty eligible for BP|
  |http://endhealth.info/qof#f11e9636-353e-434a-87db-9d25ff36ba4e|  |	[DM020] - Diabetes and no moderate/severe frailty eligible for IFCC-HbA1c|
  |http://endhealth.info/qof#72f852e3-1d0f-422d-9b55-759bbec9d71b|  |	[DM021] - Diabetes with frailty eligible for IFCC-HbA1c|
  |http://endhealth.info/qof#b01ab112-096f-4b12-9d38-4df4d7120c5c|  |	[DM034] - Diabetes aged >=40 no CVD/frailty on statin or lipid-lowering therapy|
  |http://endhealth.info/qof#53f436fa-112d-4aab-b75b-5ba6a0b97c15|  |	[DM035] - Diabetes with CVD (not haemorrhagic) on statin or lipid-lowering therapy|
  |http://endhealth.info/qof#62c2e8c2-b301-4821-9cac-9fb7d55bbb58|  |	[DM037] - Diabetes with care processes performed in last 12 months|
  |http://endhealth.info/qof#0302d204-a69d-45e3-9e4d-b5296b43f87b|  |	Patients with home BP reading as latest BP|
  |http://endhealth.info/qof#790b6e24-6c2f-4b3f-8826-f472a7e3c4bb|  |	Patients with clinic BP reading as latest BP|
  |http://endhealth.info/qof#6b922b2f-478f-48c1-9971-05133d80d9e7|  |	Latest frailty diagnosis is severe/Moderate|
  |http://endhealth.info/qof#08fc015c-4e93-44c8-9f42-ce2753b9f282|  |	Latest clinic BP >140/90 in QOF year|
  |http://endhealth.info/qof#0a04c27d-ec96-45ba-9a9b-04c6c9c1a23d|  |	Latest clinic BP > 150/90 in the QOF year|
  |http://endhealth.info/qof#7c73ac31-f7f7-479a-a244-776fb6b69eb0|  |	No BP recorded within last 12 months/latest BP record is less than last 12 months|
  |http://endhealth.info/qof#abd9ffb6-891e-4c61-99bd-ce3b70ce4f3e|  |	Patient having unresolved diabetes code|
  |http://endhealth.info/qof#f3383423-8d81-4419-bbce-bb52516670ed|  |	Patients with diabetes and smoking status as smoker or never smoked or exsmoker|
  |http://endhealth.info/qof#6198ddda-e339-402a-b482-2ea69eda71f9|  |	Diabetes quality indicator care unsuitable in the 12 months|
  |http://endhealth.info/qof#ccde0c0d-ba84-4e30-9412-9f3db3e6c66a|  |	Diabetes quality indicator care not received in last 12 months|
  |http://endhealth.info/qof#d25fc9dd-3955-4472-bfa3-68f85a96a676|  |	Earliest occurrence of diabetes is within last 3 months|
  |http://endhealth.info/qof#9b7a6851-dc67-4a54-87c6-b07686457fae|  |	Latest clinic BP above target in QOF year and 2 diabetes invitations|
  |http://endhealth.info/qof#84e19ff8-5b97-4384-9749-8520d65b0104|  |	Latest home BP above target in QOF year and 2 diabetes invitations|
  |http://endhealth.info/qof#a212ce21-faf4-4644-9beb-56f18639720b|  |	No BP recorded in last 12 months and 2 diabetes invitations|
  |http://endhealth.info/qof#9e9a4181-e9b2-4b25-9176-59d98b9dc328|  |	Diabetes diagnosis in last 9 months|
  |http://endhealth.info/qof#d2445c4a-9ca7-42b2-b411-2548be3b78f6|  |	No BP recorded within the last 12 months|
  |http://endhealth.info/qof#474294a3-8fbf-40c9-bff5-e0cf91ac3cba|  |	Latest clinic or home BP recorded within last 12 months|
  |http://endhealth.info/qof#ef5a6841-02bb-40b4-b89f-e50efd2f1382|  |	Patients on diabetes register who do not have a latest code of resolved diabetes|
  |http://endhealth.info/qof#2f1789f1-b651-460f-8c4b-3cac1833ada3|  |	Patients with DM diagnosis followed by never smoked both recorded after age 25|
  |http://endhealth.info/qof#ced8649e-c8dc-4871-8ded-d92ae8f36601|  |	Patients age <=25y with diabetes and never smoked recorded in QOF year|
  |http://endhealth.info/qof#2d467fef-836c-4111-804e-b5642db1373d|  |	[HF007] - Heart failure eligible for review of functional capacity, medication|
  |http://endhealth.info/qof#9f7d4219-e64f-4b8b-9162-78059b629b13|  |	[HF008] - Recent heart failure eligible for echocardiogram or assessment|
  |http://endhealth.info/qof#133ff3a8-0436-4fdf-8f04-5f28b760f9d4|  |	[HF009] - Heart failure due to EF eligible for ACE-I/ARB/ARNI, beta-blocker, MRA and SGLT2 inhibitor|
  |http://endhealth.info/qof#4d5d9d7a-9081-4e93-bb70-7246f9d6a607|  |	Patients selected for unresolved HF before registered in the achievement date|
  |http://endhealth.info/qof#e118ddbb-fc16-463f-9d91-75c2c080d43e|  |	Patients selected for unresolved HF before registered in the last 6 months|
  |http://endhealth.info/qof#33109f70-e47d-42ce-ba43-a8b8176ac442|  |	Persisting/expiring contraindication to ACE-I, ARB, ARNI, BB, MRA, SGLT2i|
  |http://endhealth.info/qof#afff63da-2f75-4d28-89c4-a3dc6efd1ddc|  |	Patients on ACE-I/ARB/ARNI or contraindicated, expiring or declined|
  |http://endhealth.info/qof#6fd8cf3e-572c-43cb-8a19-9872f33abf70|  |	On ÃŸ blockers, MRA & SGLT2, contraindication persisting, expiring/declined in yr|
  |http://endhealth.info/qof#e54ab125-5d2d-4d6a-b2b8-aead6dc4d11f|  |	Patients with ACE-I/ARB/ARNI contraindicated, expiring or declined this year|
  |http://endhealth.info/qof#5ddff09e-4a82-4c17-8336-db289bdc1b10|  |	[HYP010] - Hypertension age <=79 no moderate/severe frailty, BP<=140/90|
  |http://endhealth.info/qof#98f5743a-ea9f-4904-a320-750b263c3d81|  |	[HYP011] - Hypertension age >=80 no moderate/severe frailty, BP<=150/90|
  |http://endhealth.info/qof#34774fef-6f88-4776-92e4-dd49c70e1a4b|  |	Latest home BP <=135/85 in the QOF year|
  |http://endhealth.info/qof#98605924-c807-4a02-9b67-9701f162c433|  |	Latest clinic BP <=140/90 in QOF year|
  |http://endhealth.info/qof#e5b85b41-e9d5-4688-b04f-078ae1760d7a|  |	Latest home BP <=145/85 in the QOF year|
  |http://endhealth.info/qof#f6858cbc-4991-4d49-8cf5-f2b2fb2f4baa|  |	Latest clinic BP <=150/90 in QOF year|
  |http://endhealth.info/qof#be03b4d8-2b02-4d4a-b811-9bc9b80d3e24|  |	Patients are receiving maximal blood pressure therapy in the 12 months|
  |http://endhealth.info/qof#93231398-72ad-4217-9fcf-a53b0103c97e|  |	Clinic and home BP declined within 12 months before QOF year end|
  |http://endhealth.info/qof#6ed13f4d-456e-4950-a7f8-d834e05b39f0|  |	Latest home BP not recorded or > target in QOF year|
  |http://endhealth.info/qof#d3489e01-6e31-4293-907b-ef9609d5ea0e|  |	Latest home BP >145/85 in the QOF year|
  |http://endhealth.info/qof#413e753d-8f66-49ab-8bbf-ab59a093de47|  |	Hypertension quality indicator care was unsuitable in the 12 months|
  |http://endhealth.info/qof#e7b7bdc7-0185-4051-9690-eb303f8caf90|  |	patients are not receive hypertension quality indicator care in the 12 months|
  |http://endhealth.info/qof#9e57f67d-6e75-42a6-ae6b-ed442fbc6c1d|  |	Age <80y latest clinic BP above target in QOF year, 2 hypertension invitations|
  |http://endhealth.info/qof#fc4584b1-ec01-4ead-9952-85905ad58cd6|  |	Age <80y latest home BP above target in QOF year and 2 hypertension invitations|
  |http://endhealth.info/qof#f26f6681-b665-4bb0-a280-49001ac030ff|  |	No BP recorded in last 12 months and 2 hypertension invitations|
  |http://endhealth.info/qof#52e7b1a2-a9cc-4b86-bdea-e5b6ddb61c24|  |	Earliest Occurrence of Hypertension is within last 9 months|
  |http://endhealth.info/qof#1ca3bced-bdb6-4b9a-9618-2c78fb0b7f49|  |	Age >=80y latest clinic BP above target in QOF year, 2 hypertension invitations|
  |http://endhealth.info/qof#947af0dd-4bea-4249-9896-9cb6b45dc54a|  |	Age >=80y latest home BP above target in QOF year and 2 hypertension invitations|
  |http://endhealth.info/qof#680eae71-db9a-497c-93c0-6f50d81594d9|  |	Latest clinic BP in QOF year and 2 hypertension invitations|
  |http://endhealth.info/qof#e1c5488b-8ee4-4a39-875b-7fdb810eba1c|  |	Latest home BP in QOF year and 2 hypertension invitations|
  |http://endhealth.info/qof#7d1dbc05-6488-4baa-b5d1-91bdda9bede6|  |	Patients on hypertension register without latest code of unresolved hypertension|
  |http://endhealth.info/qof#68736d91-df17-4e33-9d9b-6b2355f61fd8|  |	[MH002] - Eligible for documented care plan|
  |http://endhealth.info/qof#be72fcfd-03f5-43bc-9a62-ac0a5acd6deb|  |	[MH003] - Eligible for BP in the preceding 12 months|
  |http://endhealth.info/qof#382cdc69-0b25-4a83-8cbd-ebac78312a38|  |	[MH006] - Eligible for BMI in the preceding 12 months|
  |http://endhealth.info/qof#ada06d96-b02a-4ce1-aa54-71252a5db585|  |	[MH011] - Eligible for lipid profile recorded in the preceding 12 months|
  |http://endhealth.info/qof#377e0a21-39e9-40cd-b36f-4dc8d11d75d4|  |	[MH007] - Eligible for alcohol recorded in the preceding 12 months|
  |http://endhealth.info/qof#f507e7fa-e26e-4f91-a057-490ab9981ca6|  |	[MH012] - Eligible for glucose/Hba1c recorded in the preceding 12 months|
  |http://endhealth.info/qof#c082a9dc-23ad-464c-827f-56258ca40b57|  |	Antipsychotics in last 6m of QOF yr, CVS risk factor, smoker (DM earliest)|
  |http://endhealth.info/qof#72627532-a436-446b-99eb-d48580c1fd10|  |	No antipsychotics in last 6m of QOF yr, CVS risk factor, smoker (DM earliest)|
  |http://endhealth.info/qof#ac2781fa-8e28-485c-bb33-bce45b16afb1|  |	Not in remission and most recent code for schizophrenia/BPAD/other psychoses|
  |http://endhealth.info/qof#7aa091dd-2414-4ae2-8105-4410c3b3b290|  |	Mental health quality indicator care deemed unsuitable within 1y before QOF year end|
  |http://endhealth.info/qof#4b9f211a-5ab8-4015-8c89-4fe09556701a|  |	Declined mental health quality indicator within 1y before QOF year end|
  |http://endhealth.info/qof#e474ec17-35b8-4abb-be72-a75ef97fa283|  |	Not responded to 2 or more mental health invitations within 1y before QOF yr end|
  |http://endhealth.info/qof#e6171921-73c3-40ee-bc05-6e91b6994a50|  |	Earliest Psychosis, Schizophrenia or Bipolar Illness diagnosed in last 3 months|
  |http://endhealth.info/qof#004a279b-6298-4ddd-bdc5-ea32a3cad817|  |	Most recent ethnicity code is White ethnic background|
  |http://endhealth.info/qof#f946de0b-1241-4e01-babb-7fc24832e052|  |	Most recent BMI code indicates overweight/obese or has value >=25|
  |http://endhealth.info/qof#dbbbfa13-f09a-45dd-81bb-a708b7feafe1|  |	Most recent BMI code indicates overweight/obese or has value >=23|
  |http://endhealth.info/qof#730d1248-6574-45e4-b51b-4f78b6478bbd|  |	Most recent ethnicity code is not White ethnic background|
  |http://endhealth.info/qof#38688cd8-d181-4546-9fec-45a1154f5d5b|  |	BMI >=25 and White ethnicity, BMI >=23 not White, BMI >=30/overweight/obese|
  |http://endhealth.info/qof#0473c3d2-0824-4f2e-9e27-01a9d906cf91|  |	No BMI>=25 and White ethnicity, BMI>=23 not White, BMI>=30/overweight/obese|
  |http://endhealth.info/qof#63680cb8-3d30-482f-a40e-1c6009dbf294|  |	[STIA007] - Non-haemorrhagic stroke or TIA eligible for anti-platelet or anti-coagulant|
  |http://endhealth.info/qof#6eeff630-c46e-421f-bfbe-bad48751e0d2|  |	No BP recorded in last 12 months and 2 stroke invitations|
  |http://endhealth.info/qof#daa97287-03e2-4f6e-a57f-0882a7210483|  |	Latest clinic BP in QOF year and 2 stroke invitations|
  |http://endhealth.info/qof#671799a6-18b2-436f-861b-9ccbbd07074d|  |	Latest home BP in QOF year and 2 stroke invitations|
  |http://endhealth.info/qof#7462bb9d-8454-4432-ac33-0789610c8b7c|  |	Age <80y latest clinic BP above target in QOF year and 2 stroke invitations|
  |http://endhealth.info/qof#69de504f-a026-44d6-ac09-1791b0407024|  |	Age <80y latest home BP above target in QOF year and 2 stroke invitations|
  |http://endhealth.info/qof#e4fa7957-b913-47a4-b617-4b1bc37439fd|  |	Age >=80y latest clinic BP above target in QOF year + 2 stroke invitations|
  |http://endhealth.info/qof#176485ca-46e2-4d40-9f78-09251ce4d873|  |	Age >=80y latest home BP above target in QOF year and 2 stroke invitations|
  |http://endhealth.info/qof#be1ab1b1-a6d5-475e-97c4-2ce091f4c189|  |	VICC001 - Patients who attained 8 months old during the service year.|
  |http://endhealth.info/qof#4a8341aa-99dc-461d-a3c1-b798182c431e|  |	VICC002 - Patients who attained 18 months old during the service year.|
  |http://endhealth.info/qof#9bc63b67-5fd6-4f4c-90fb-8bebcc8659dd|  |	VICC003 - Patients who attained 5 years old during the service year.|
  |http://endhealth.info/qof#0db5ad77-2fa5-4535-ad7c-56d1b78518f5|  |	VICC004 - Patients who attained 80 or 81 years old during the service year.|
  |http://endhealth.info/qof#ab9802fc-b445-4987-89a4-714a5ff3d1de|  |	A booster dose of DTaP/IPV vaccine whilst they were less than 5 years of age.|
  |http://endhealth.info/qof#e50d94c3-f970-4637-b79e-220871f0aa23|  |	Under 18m old within the 12 months before QOF year end|
  |http://endhealth.info/qof#7388dcfc-0f8f-4e86-806f-2393923ae7a5|  |	Under 5y old within the 12 months before QOF year end|
  |http://endhealth.info/qof#1c47eb51-97ff-45d6-a0a6-67f938e19359|  |	Age < 80y within the 12 months before QOF year end|
  |http://endhealth.info/qof#a3014db1-8559-456d-ab54-7f7f3f2ecb24|  |	Patients aged 80 years|
  |http://endhealth.info/qof#ac4d04ee-b3bd-41eb-b775-562777920512|  |	Age < 81y within the 12 months before QOF year end|
  |http://endhealth.info/qof#69f7ccda-59c5-42c2-a450-35463aa5a001|  |	Patients with no 1st MMR before registration|
  |http://endhealth.info/qof#0cd6b24e-2f55-4fd3-b37d-c8d6e36475f6|  |	Registered after 17m with no 1st MMR before registration|
  |http://endhealth.info/qof#10dac62a-d60f-467a-94ba-2c5bd7cca70c|  |	A dose of DTaP/IPV vaccine is contraindicated before the patient's 5th birthday.|
  |http://endhealth.info/qof#1c6110d2-8c64-47e6-9850-66078b92b089|  |	Patients with no DTaP/IPV before registration|
  |http://endhealth.info/qof#a24c429e-df2c-44d6-baf3-dd856a4fa965|  |	Patients with 1 MMR dose before registration|
  |http://endhealth.info/qof#9cb79cd0-882b-4058-83c0-b0e7a7a4d354|  |	Patients with no 2nd dose MMR or patients with 2nd dose MMR after registration|
  |http://endhealth.info/qof#55883671-089d-4ee6-bad6-e85c3e8ff0e1|  |	Patients with 2 doses of MMR vaccine before registration|
  |http://endhealth.info/qof#b8529073-afc1-4ae4-8013-611ebb77926d|  |	Patients with DTaP/IPV booster before registration|
  |http://endhealth.info/qof#5f50c9dc-1aa7-42e7-baf2-0a9a89cd826f|  |	Registered after 4y9m with no MMR or DTaP/IPV booster before registration|
  |http://endhealth.info/qof#812c7c23-34af-4bd6-b3ff-7e53b7f9d0af|  |	Registered after 4y10m with 1 MMR dose + no DTaP/IPV booster before registration|
  |http://endhealth.info/qof#12b90a8a-2443-45f2-9ea6-1eea0811a26a|  |	Registered after 4y10m with DTaP/IPV booster + no MMR before registration|
  |http://endhealth.info/qof#696ba149-e521-423a-b36c-30d61626017b|  |	Registered after 4y11m with 2 MMR dose + no DTaP/IPV booster before registration|
  |http://endhealth.info/qof#c15f4ffb-5c49-43ac-881c-d13f32425b9b|  |	Registered after 4y11m with 1 MMR dose and DTaP/IPV booster before registration|
  |http://endhealth.info/qof#fe648300-d880-41f6-841a-928d16026da2|  |	Turned 81 this QOF year and had 2nd shingles dose between ages 70-79 (inclusive)|
  |http://endhealth.info/qof#95d2c986-4dfa-4965-b3aa-96736efaa36c|  |	Turned 81 this QOF year and had 1st shingles dose between ages 70-79 (inclusive)|
  |http://endhealth.info/qof#2a275f01-42eb-448f-8057-cb67cdeabaa3|  |	Patient aged 80 within the service year|
  |http://endhealth.info/qof#e5d5a28f-ee74-4407-9520-accd800a7860|  |	Patients with 0-2 doses of DTaP before registration|
  |http://endhealth.info/qof#5d6208f6-9caa-40ef-96f6-b6accafc637e|  |	Registered at 7m+ with 0-2 doses of DTaP before registration|
  |http://endhealth.info/qof#966f86bc-e729-4eef-9c2c-1cc0db5a2f54|  |	Patients with no 2nd dose DTaP or patients with 2nd dose DTaP after registration|
  |http://endhealth.info/qof#1252b126-5ebb-4133-b35d-73b958154993|  |	Registered at 6m+ with 0-1 DTaP doses before registration|
  |http://endhealth.info/qof#df9c31f3-d5ae-4edf-b5e0-6711088b165e|  |	Patients with no DTaP doses before registration|
  |http://endhealth.info/qof#07d0ac46-3d8a-475b-8b85-5f29aee6485c|  |	Registered at 5m+ with no DTaP doses before registration|
  |http://endhealth.info/qof#9e9dfc61-0d96-4fcb-a4b0-5d0120ec672a|  |	[VI001] - Three or more DTaP vaccine doses before 8 months|
  |http://endhealth.info/qof#dfa3b3be-9f32-4d5c-9552-d998fdf7d5b7|  |	[VI002] - One or more MMR or MMRV doses aged 12-18 months|
  |http://endhealth.info/qof#4ee0a05c-d045-4aa2-9520-45eb2c69c8ac|  |	[VI003] - Aged 1-5 years with DTaP/IPV booster and at least 2 doses MMR or MMRV|
  |http://endhealth.info/qof#2a2b8f0f-9f38-460c-9f59-2862b545ab52|  |	[VI004] - Aged 80 with shingles vaccine received between 70-79 years|
  |http://endhealth.info/qof#776c535f-5b32-43a8-b324-9abe799242ef|  |	[CHOL003] - Patients with CHD / PAD / TIA / CKD on statin/lipid-lowering therapy|
  |http://endhealth.info/qof#b848cdbe-84ec-4ab8-bc98-286996a32037|  |	[CHOL004] - Patients with LDL cholesterol <=2.0 or non-HDL <=2.6|
  |http://endhealth.info/qof#c8dca4dc-3b67-4f83-b711-252b44f11578|  |	Statin intolerance / declined or persisting / expiring statin contraindication|
  |http://endhealth.info/qof#824786e7-f2c8-4431-956c-055fc3ecc0ed|  |	Prescribed lipid lowering therapy within the last 6 months|
  |http://endhealth.info/qof#dc2f74ec-e690-40f1-a9ac-339c124fbba9|  |	Bempedoic acid allergy, reaction, contraindication, not indicated or declined|
  |http://endhealth.info/qof#cba8dc50-045e-4c8b-9601-dc2e7a6ab843|  |	Ezetimibe allergy, reaction, contraindication, not indicated or declined|
  |http://endhealth.info/qof#d26a2240-8648-44ee-9ab4-67c0b99d42df|  |	Inclisiran allergy, reaction, contraindication, not indicated or declined|
  |http://endhealth.info/qof#6eb7129c-3b6c-4f62-a002-926e1c6dd238|  |	PCSK9 inhibitor allergy, reaction, contraindication, not indicated or declined|
  |http://endhealth.info/qof#33612278-79eb-4165-a8cf-b21118b51f4e|  |	Prescribed a statin within the last 6 months|
  |http://endhealth.info/qof#4089b32a-75a5-4f85-8127-fda3867834fa|  |	Patient with non-HDL cholesterol <=2.6 mmol/L and no record of LDL cholesterol|
  |http://endhealth.info/qof#aaafc0e2-26c0-4da4-9be2-8b901ec612e2|  |	Latest LDL cholesterol above target in QOF year and 2 invitations|
  |http://endhealth.info/qof#881f9c57-35b2-4a6e-8294-4a04a3ba58d1|  |	Latest non-HDL cholesterol above target in QOF year and 2 invitations|
  |http://endhealth.info/qof#3ac51a7f-35d5-4431-9901-94f4ad1bb59f|  |	2 cholesterol invitations + no cholesterol recorded within the last 12 months|
  |http://endhealth.info/qof#b29cc44c-0800-4aa6-b74d-d3128118e19b|  |	OSTEO1_REG and OSTEO2_REG combined - Patients on the osteoporosis register|
  |http://endhealth.info/qof#260fa47f-17a7-4774-a0f5-7b9870132106|  |	[BP002] - Patients age 45 or above for BP Check|
  |http://endhealth.info/qof#31868ea0-aeea-4c59-b1d9-ef839a1aea65|  |	[CS005] - Aged 25-49 eligible for cervical screening|
  |http://endhealth.info/qof#0e3773f7-29f6-4d43-b744-f84347c40eb8|  |	[CS006] - Aged 50-64 eligible for cervical screening|
  |http://endhealth.info/qof#73574556-1e01-4059-9bc8-c7bc8997b6e2|  |	Female Patients in Smear Target Age Range|
  |http://endhealth.info/qof#f5b52d59-9e36-42ea-b673-41e7b21b6e10|  |	[OB004] - Patients age >=18 with obesity referred to weight management|
  |http://endhealth.info/qof#4f62b238-3298-48cd-bae7-ea69a33fcf29|  |	[OB005] - Patients NICE TA1026 cohort,shared decision,meds,behavioural referral|
  |http://endhealth.info/qof#26dd6bb9-3681-44e8-8815-d2cf84889618|  |	Recorded ethnic minority groups|
  |http://endhealth.info/qof#99480408-8c8f-4328-a57f-7f71234d1fe7|  |	ASCVD, dyslipidaemia, obstructive sleep apnoea, hypertension, type 2 diabetes|
  |http://endhealth.info/qof#e77fc816-2f4a-48be-a8b4-6ca02ddecfcd|  |	Date of weight management referral recorded within 90 days on or after high BMI|
  |http://endhealth.info/qof#c4d337c5-0946-43e0-b2fb-998117e9c2ae|  |	Patient selected in Rule 2 of OB004|
  |http://endhealth.info/qof#5d2a8b69-9aec-4cbc-b12d-1eab0689c1a1|  |	High BMI recorded within the reporting year|
  |http://endhealth.info/qof#5521d3f6-0d9f-4f87-ae80-321bc22b4f27|  |	Patient selected in Rule 4 of OB005|
  |http://endhealth.info/qof#8ff6ba4e-49eb-4d5d-a0ec-ccab3a237c6e|  |	Patient selected in Rule 9 of OB004|
  |http://endhealth.info/qof#fb3526cb-1070-49e0-92e3-3317bf59a0f2|  |	Patient selected in Rule 15 of OB005|
  |http://endhealth.info/qof#e18dda68-420b-44d5-b3df-f78978db69ea|  |	Patients on lipid-lowering therapy prescribed in 6m and within achievement date|
  |http://endhealth.info/qof#28af2289-bac7-4c0a-9889-f742ae9d2b99|  |	Prescribed lipid in 6m/LDL >=4.1/triglycerides >=1.7/HDL(<1.0 male/<1.3 female)|
  |http://endhealth.info/qof#a1ae0a7e-513c-46e3-acf3-8db6ed5596c7|  |	Obese patients with: BP, Lipids, OSA & T2DM|
  |http://endhealth.info/qof#58d7d349-17f4-4dea-9dad-28ed1c159df3|  |	Obese patients with: ASCVD, Lipids, OSA & T2DM|
  |http://endhealth.info/qof#6a21ca81-0fac-4ebe-b9e5-c71eb00cb606|  |	Obese patients with: ASCVD, BP, OSA & T2DM|
  |http://endhealth.info/qof#79dd0c41-aedb-446c-93ab-acc586349149|  |	Obese patients with: ASCVD, BP, Lipids & T2DM|
  |http://endhealth.info/qof#fb3b18f4-1939-48a3-9d13-70a436dc2e80|  |	Obese patients with: ASCVD, BP, Lipids & OSA|
  |http://endhealth.info/qof#a761817a-9a7d-4446-8610-452388319d9c|  |	Weight management referral within start to 90 days after prior-year high BMI|
  |http://endhealth.info/qof#e037ec61-2601-4a35-b620-215782e37b91|  |	First BMI >=30 kg/m2 recorded in last 90 days of prior reporting year|
  |http://endhealth.info/qof#39b85cc4-3e57-4dd8-a6f8-18ddb2fb0228|  |	Earliest high BMI in reporting year recorded within last 90 days.|
  |http://endhealth.info/qof#1381c25b-a2a4-4bda-8619-03a1aa1a55a1|  |	No high BMI recorded within the previous reporting year|
  |http://endhealth.info/qof#0e51815d-6436-4a68-98d1-4d7e02ffd3f7|  |	Earliest high BMI recorded in previous reporting year, before last 90 days|
  |http://endhealth.info/qof#33b5ccd9-3697-4f9a-a8f5-f3a65bfbc6d1|  |	Earliest high BMI recorded in previous reporting year, within last 90 days|
  |http://endhealth.info/qof#7230eedf-e100-445b-bcda-eda1b7301a3e|  |	Legitimate PCA (for indicator OB004) recorded in the previous reporting year|
  |http://endhealth.info/qof#fedc55ef-ecff-474a-bb99-e19cc1da8628|  |	Prior-year earliest high BMI within last 6 months with achievement or PCA coded|
  |http://endhealth.info/qof#8492a8ce-f015-4cf7-9c61-f084542d94e0|  |	[SMOK004] - Current smokers eligible for an offer of support|
  |http://endhealth.info/qof#1cb1523b-633c-4c2b-ba42-cef2b9b9d40a|  |	[SMOK002] - Chronic disease eligible for smoking status recorded|
  |http://endhealth.info/qof#016cafc7-40f9-4c32-9033-9045eca2b146|  |	Ex-smoker for 3 consecutive years with no later current smoking record|
  |http://endhealth.info/qof#1cdc9251-4337-45bc-9e2d-0e9f5fb562ad|  |	Patients with 'never smoked' status recorded on or after diagnosis date|
  |http://endhealth.info/qof#a8431d57-b198-4326-889d-de36b3064a0b|  |	Recent onset CVS risk factors, DM, asthma, COPD, psychosis, schizophrenia or BPAD|
  |http://endhealth.info/qof#4fb5ad12-4122-45fb-8244-f18b536270a3|  |	Patients who have ever had CKD Stage 3 to 5|
  |http://endhealth.info/qof#ca57662f-ecc7-41d6-b561-4af63b182b01|  |	LTC Manager Search - AF annual review absent in QOF year 26-27|
  |http://endhealth.info/qof#64792e1d-5743-43a6-be53-debef9d81219|  |	LTC Manager Search - Asthma annual review absent in QOF year 26-27|
  |http://endhealth.info/qof#E0D75A49-167D-4205-97FB-FDB29225B6D2|  |	LTC Manager Search - CHD annual review absent in QOF 26-27|
  |http://endhealth.info/qof#c8ca80c5-92a8-4740-bde5-6cf11c95153b|  |	LTC Manager Search - COPD annual review absent in QOF 26-27|
  |http://endhealth.info/qof#95d6691d-6b4c-4bf7-9cd5-c1599e734782|  |	LTC Manager Search - Diabetes annual review absent in QOF 26-27|
  |http://endhealth.info/qof#44ed01cd-7e2d-49a4-827a-5dd547222af3|  |	LTC Manager Search - Heart failure with LVSD annual review absent in QOF 26-27|
  |http://endhealth.info/qof#55bd3af4-8a5e-4a4b-b02a-3cf727c155bf|  |	LTC Manager Search - HF without LVSD annual review absent in QOF 26-27|
  |http://endhealth.info/qof#deabbba8-386b-47ae-8a58-4360522027a4|  |	LTC Manager Search - Hypertension annual review absent in QOF 26-27|
  |http://endhealth.info/qof#d4fea1d6-8f12-41de-aa94-c98d911716e4|  |	LTC Manager Search - PAD annual review absent in QOF 26-27|
  |http://endhealth.info/qof#e67ffe28-9e32-4cc8-bac5-05d70f88e81a|  |	LTC Manager Search - Stroke and TIA annual review absent in QOF 26-27|
  |http://endhealth.info/qof#1f72e199-1a73-434f-9381-ee2308e6e651|  |	Palliative care commenced in last 2 years|
