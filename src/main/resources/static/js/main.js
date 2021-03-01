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
                    {key: 'clientId', label: 'client id', sortable: true},
                    {key: 'appName', label: '앱 이름', sortable: true},
                    {key: 'modifiedDate', label: '수정일시', sortable: true},
                    {key: 'actions', label: 'Actions'}
                ],
                items: []
            }
            , manage: {
                mode: 'register',
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
        webhook: {
            list: {
                fields: [
                    {key: 'idx', label: 'IDX', sortable: true},
                    {key: 'xTraceId', label: 'x-trace-id', sortable: true},
                    {key: 'eventNo', label: '이벤트', sortable: true},
                    {key: 'resource', label: '수신 데이터', sortable: true},
                    {key: 'createdDate', label: '수신 일시', sortable: true},
                    {key: 'actions', label: 'Actions'}
                ],
                items: []
            }
        }
    },
    computed: {
        stateAppName() {
            return this.app.manage.values.appName.length > 3
        },
        stateClientId() {
            return this.app.manage.values.clientId.length > 20
        },
        stateSecretKey() {
            return this.app.manage.values.secretKey.length > 20
        },
        scopesStr() {
            return this.app.manage.values.scopes.join(',');
        }
    },
    methods: {
        info(item, index, button) {
            this.app.manage.mode = 'modify';
            this.$root.$emit('bv::show::modal', 'modal-manage-app', button);

            this.app.manage.values.idx = item.idx;
            this.app.manage.values.appName = item.appName;
            this.app.manage.values.clientId = item.clientId;
            this.app.manage.values.secretKey = '###############';
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
                        ita.app.list.items.push(res.data.data);

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
        },
        //앱 등록 모달 데이터 초기화
        createModalInit: function () {
            this.app.manage.mode = 'register';
            this.app.manage.values = JSON.parse(JSON.stringify(this.app.manage.defaultValues));
        },
        queryStr: function (params) {
            if (params === undefined) {
                return '';
            }
            return '?' + Object.entries(params).map(e => e.join('=')).join('&');
        }
    },
    created() {
        var searchValue = document.getElementsByClassName('inp-search-client-id');
        if (searchValue != null) {
            this.searchForm.clientId = searchValue[0].dataset.client_id;
        }

        this.getScopeOption();
        this.createModalInit();
        this.getApps();
        this.getWebhooks();
    },
    mounted() {
    }
})
