window.ita = new Vue({
    el: '#ita',
    data: {
        searchForm: {
            clientId: ''
        },
        app: {
            list: {
                fields: [
                    {key: 'idx', label: 'IDX', sortable: true},
                    {key: 'clientId', label: 'client-id', sortable: true},
                    {key: 'appName', label: '앱 이름', sortable: true},
                    {key: 'modifiedDate', label: '수정일시', sortable: true},
                    {key: 'actions', label: '관리'}
                ],
                items: []
            }
            , manage: {
                mode: 'register',
                modifyIdx: 0,
                values: {},
                defaultValues: {
                    appName: '',
                    clientId: '',
                    secretKey: '',
                    partnerId: '',
                    grantType: 'code',
                    manageToken: 'refresh',
                    operationLevel: 'qa',
                    scopes: []
                },
                options: {
                    grantType: [{text: 'code', value: 'code'}, {text: 'credential', value: 'credential'}],
                    manageToken: [{text: '갱신', value: 'refresh'}, {text: '유지', value: 'keep'}],
                    operationLevel: [
                        {text: 'QA', value: 'qa'},
                        {text: 'AUTO', value: 'auto'},
                        {text: 'DEV', value: 'dev'},
                        {text: 'PROD', value: 'prod'}
                    ],
                    scopes: []
                }
            }
        },
        api: {
            list: {
                fields: [
                    {key: 'idx', label: 'IDX', sortable: true},
                    {key: 'clientId', label: 'client-id', sortable: true},
                    {key: 'mallId', label: 'mall-id', sortable: true},
                    {key: 'method', label: 'method', sortable: true},
                    {key: 'version', label: 'version', sortable: true},
                    {key: 'createdDate', label: '호출일시', sortable: true},
                    {key: 'actions', label: '관리'}
                ],
                items: []
            }
            , manage: {
                modifyIdx: 0,
                values: {},
                defaultValues: {
                    mallId: '',
                    clientId: '',
                    method: 'GET',
                    version: '',
                    apiUrl: '',
                    requestBody: '',
                    response: ''
                },
                options: {
                    methods: [
                        {text: 'Get', value: 'GET'},
                        {text: 'Post', value: 'POST'},
                        {text: 'Put', value: 'PUT'},
                        {text: 'Delete', value: 'DELETE'}
                    ],
                    mallIds: [
                        {text: 'jhbaek02', value: 'jhbaek02'},
                        {text: 'Post', value: 'post'},
                        {text: 'Put', value: 'put'},
                        {text: 'Delete', value: 'delete', disabled: true}
                    ],
                    clientIds: [
                        {text: 'SYCZyiYVH5ppkJLiVdiniB', value: 'SYCZyiYVH5ppkJLiVdiniB'},
                        {text: 'Post', value: 'post'},
                        {text: 'Put', value: 'put'},
                        {text: 'Delete', value: 'delete', disabled: true}
                    ]
                }
            }
        },
        webhook: {
            list: {
                fields: [
                    {key: 'idx', label: 'IDX', sortable: true},
                    {key: 'clientId', label: 'client-id', sortable: true},
                    {key: 'xTraceId', label: 'x-trace-id', sortable: true},
                    {key: 'eventNo', label: '이벤트', sortable: true},
                    {key: 'resource', label: '수신 데이터', sortable: false},
                    {key: 'createdDate', label: '수신 일시', sortable: true},
                    {key: 'actions', label: '관리'}
                ],
                items: []
            }
        }
    },
    computed: {
        stateAppName() {
            let length = this.app.manage.values.appName.length;
            return length >= 3 && length <= 20;
        },
        stateClientId() {
            return this.app.manage.values.clientId.length === 22
        },
        stateSecretKey() {
            return this.app.manage.values.secretKey.length === 22
        },
        stateScope() {
            return this.app.manage.values.scopes.length > 0;
        },
        scopesStr() {
            return this.app.manage.values.scopes.join(',');
        },
        webhooksCount() {
            return this.webhook.list.items.length;
        },
        appsCount() {
            return this.app.list.items.length;
        },
        isRegisterApp() {
            return this.app.manage.mode === 'register';
        }
    },
    methods: {
        setAppValue(item, index, button) {
            this.app.manage.mode = 'modify';
            this.app.manage.modifyIdx = index;
            this.$root.$emit('bv::show::modal', 'modal-manage-app', button);

            this.app.manage.values.idx = item.idx;
            this.app.manage.values.appName = item.appName;
            this.app.manage.values.clientId = item.clientId;
            this.app.manage.values.secretKey = '######################';
            this.app.manage.values.partnerId = item.partnerId
            this.app.manage.values.grantType = item.grantType;
            this.app.manage.values.manageToken = item.manageToken;
            this.app.manage.values.operationLevel = item.operationLevel;
            this.app.manage.values.scopes = item.scopes;
        },
        //App 리스트 조회
        getApps: function () {
            axios.get(CONTEXT_PATH + '/api/v1/apps' + this.queryStr(ita.searchForm))
                .then(function (res) {
                    if (res.data.code !== 200) {
                        console.error(res);
                        alert("res.data.message");
                        return;
                    }

                    ita.app.list.items = res.data.data;
                }, function (err) {
                    console.error(err);
                });
        }, //api 리스트 조회
        getApis: function () {
            axios.get(CONTEXT_PATH + '/api/v1/apis' + this.queryStr(ita.searchForm))
                .then(function (res) {
                    if (res.data.code !== 200) {
                        console.error(res);
                        alert("res.data.message");
                        return;
                    }

                    ita.api.list.items = res.data.data;
                }, function (err) {
                    console.error(err);
                });
        }, //webhook 리스트 조회
        getWebhooks: function () {
            axios.get(CONTEXT_PATH + '/api/v1/webhooks' + this.queryStr(ita.searchForm))
                .then(function (res) {
                    if (res.data.code !== 200) {
                        console.error(res);
                        alert("res.data.message");
                        return;
                    }

                    ita.webhook.list.items = res.data.data;
                }, function (err) {
                    console.error(err);
                });
        },
        //스코프 옵션 초기화
        getScopeOption: function () {
            axios.get(CONTEXT_PATH + '/api/v1/scopes/options', [])
                .then(function (res) {
                    if (res.data.code !== 200) {
                        console.error(res);
                        alert("스코프 옵션 세팅에 실패했습니다.");
                        return;
                    }

                    ita.app.manage.options.scopes = res.data.data;
                }, function (err) {
                    console.error(err);
                });
        },
        //앱 등록
        registerApp: function () {
            axios.post(CONTEXT_PATH + '/api/v1/app', this.app.manage.values)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.$bvModal.hide("modal-manage-app");
                        ita.app.list.items.push(res.data.data);

                        return;
                    }

                    console.error(res);
                    alert("앱 등록에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                });
        },//앱 수정
        modifyApp: function () {
            let sUrl = CONTEXT_PATH + '/api/v1/app/' + this.app.manage.values.idx;
            axios.put(sUrl, this.app.manage.values)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.$bvModal.hide("modal-manage-app");
                        ita.app.list.items.splice(ita.app.manage.modifyIdx, 1, res.data.data);

                        return;
                    }

                    alert("앱 수정에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                });
        },//앱 삭제
        deleteApp: function (idx) {
            if (confirm(this.app.list.items[idx].appName + '를 삭제하시겠습니까?') === false) {
                return;
            }

            let sUrl = CONTEXT_PATH + '/api/v1/app/' + this.app.list.items[idx].idx;
            axios.delete(sUrl)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.app.list.items.splice(idx, 1);

                        return;
                    }

                    alert("앱 삭제에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                });
        },//웹훅 삭제
        deleteWebhook: function (idx) {
            if (confirm(this.webhook.list.items[idx].xTraceId + '를 삭제하시겠습니까?') === false) {
                return;
            }

            let sUrl = CONTEXT_PATH + '/api/v1/webhooks/' + this.webhook.list.items[idx].idx;
            axios.delete(sUrl)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.webhook.list.items.splice(idx, 1);

                        return;
                    }

                    alert("웹훅 삭제에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                });
        },
        //웹훅 전체 삭제
        deleteWebhookAll: function (idx) {
            if (confirm('수신한 전체 웹훅을 삭제하시겠습니까?') === false) {
                return;
            }

            let sUrl = CONTEXT_PATH + '/api/v1/webhooks';
            axios.delete(sUrl)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.webhook.list.items = [];

                        return;
                    }

                    alert("웹훅 삭제에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                });
        },
        //API 호출
        callApi: function () {
            axios.post(CONTEXT_PATH + '/api/v1/api', this.api.manage.values)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.api.list.items.push(res.data.data);
                        ita.api.manage.values = res.data.data;

                        return;
                    }

                    console.error(res);
                    alert("API 호출에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                });
        },
        //앱 등록 모달 데이터 초기화
        createAppModalInit: function () {
            this.app.manage.mode = 'register';
            this.app.manage.values = JSON.parse(JSON.stringify(this.app.manage.defaultValues));
        }, //api 호출 모달 데이터 초기화
        callApiModalInit: function () {
            this.api.manage.values = JSON.parse(JSON.stringify(this.api.manage.defaultValues));
        },
        queryStr: function (params) {
            if (params === undefined) {
                return '';
            }
            return '?' + Object.entries(params).map(e => e.join('=')).join('&');
        },
        searchAll: function () {
            this.getApps();
            this.getWebhooks();
        },
        copyUrl: function (event) {
            var copyText = document.getElementById('common-input-copy');
            copyText.value = event.target.parentElement.getElementsByClassName('common-url')[0].textContent;
            copyText.style.display = '';
            copyText.select();
            copyText.setSelectionRange(0, 99999);
            document.execCommand("copy");
            copyText.style.display = 'none';
        }
    },
    created() {
        var searchValue = document.getElementsByClassName('inp-search-client-id');
        if (searchValue != null) {
            this.searchForm.clientId = searchValue[0].dataset.client_id;
        }

        this.getScopeOption();
        this.createAppModalInit();
        this.getApps();
        this.getApis();
        this.getWebhooks();
    },
    mounted() {
    }
})
