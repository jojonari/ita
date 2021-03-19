window.ita = new Vue({
    el: '#ita',
    data: {
        searchForm: {
            clientId: ''
        },
        app: {
            list: {
                sort: 'idx',
                sortDesc: true,
                page: 0,
                isBusy: false,
                fields: [
                    {key: 'idx', label: 'IDX', sortable: true},
                    {key: 'clientId', label: 'client-id', sortable: true},
                    {key: 'appName', label: '앱 이름', sortable: true},
                    {key: 'partnerId', label: '파트너 아이디', sortable: true},
                    {key: 'grantType', label: '인증 방법', sortable: true},
                    {key: 'operationLevel', label: '운영 환경', sortable: true},
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
                    grantType: [],
                    manageToken: [{text: '갱신', value: 'refresh'}, {text: '유지', value: 'keep'}],
                    operationLevel: [],
                    scopes: []
                }
            }
        },
        api: {
            list: {
                sort: 'idx',
                sortDesc: true,
                page: 0,
                isBusy: false,
                fields: [
                    {key: 'idx', label: 'IDX', sortable: true},
                    {key: 'clientId', label: 'client-id', sortable: true},
                    {key: 'mallId', label: 'mall-id', sortable: true},
                    {key: 'version', label: 'version', sortable: true},
                    {key: 'method', label: 'method', sortable: true},
                    {key: 'path', label: 'path', sortable: true},
                    {key: 'requestBody', label: 'requestBody', sortable: false},
                    {key: 'response', label: 'response', sortable: false},
                    {key: 'createdDate', label: '호출일시', sortable: true},
                    {key: 'actions', label: '관리'}
                ],
                items: []
            }
            , manage: {
                apiCalling: false,
                values: {},
                defaultValues: {
                    mallId: '',
                    clientId: '',
                    method: 'GET',
                    version: '',
                    path: '',
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
                    mallIds: [],
                    clientIds: []
                }
            }
        },
        webhook: {
            list: {
                sort: 'idx',
                sortDesc: true,
                page: 0,
                isBusy: false,
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
        stateApp_AppName() {
            let length = this.app.manage.values.appName.length;
            return length >= 3 && length <= 20;
        },
        stateApp_ClientId() {
            return this.app.manage.values.clientId.length === 22
        },
        stateApp_SecretKey() {
            return this.app.manage.values.secretKey.length === 22
        },
        stateApp_Scope() {
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
        }, apisCount() {
            return this.api.list.items.length;
        },
        isRegisterApp() {
            return this.app.manage.mode === 'register';
        },
        stateApi_ClientId() {
            this.setMallIdsApiOption();
            return this.api.manage.values.clientId !== '';
        },
        stateApi_MallId() {
            return this.api.manage.values.mallId !== '';
        },
        stateApi_Path() {
            let path = this.api.manage.values.path || '';
            return path.indexOf('/api/v2/') === 0;
        },
        stateApi_Version() {
            if (this.api.manage.values.version === '') {
                return true;
            }

            return this.regexVersion();
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
        retryCallApi(item, button) {
            this.api.manage.values.mallId = item.mallId;
            this.api.manage.values.clientId = item.clientId;
            this.api.manage.values.method = item.method;
            this.api.manage.values.version = item.version
            this.api.manage.values.path = item.path;
            this.api.manage.values.requestBody = item.requestBody;
            this.api.manage.values.response = '';
            this.setClientIdsApiOption();

            this.$root.$emit('bv::show::modal', 'modal-manage-api', button);
        },
        //App 리스트 조회
        getApps: function (addPage) {
            this.getListData(this.app.list, '/api/v1/apps', addPage);
        },
        //api 리스트 조회
        getApis: function (addPage) {
            this.getListData(this.api.list, '/api/v1/apis', addPage);
        }, //webhook 리스트 조회
        getWebhooks: function (addPage) {
            this.getListData(this.webhook.list, '/api/v1/webhooks', addPage);
        },
        //통합조회
        getListData: function (list, path, addPage) {
            var query = this.searchForm ? JSON.parse(JSON.stringify(this.searchForm)) : {};
            query.sort = list.sort + ',' + (list.sortDesc ? 'desc' : 'asc');
            query.page = list.page + (addPage || 0);

            if (query.page < 0) {
                this.infoToast('첫 페이지 입니다.');
                return;
            }

            list.isBusy = true;
            axios.get(CONTEXT_PATH + path + this.queryStr(query))
                .then(function (res) {
                    if (res.data.code !== 200) {
                        console.error(res);
                        ita.errorToast(res.data.message);
                        list.isBusy = false;

                        return;
                    }

                    if (res.data.pageable.page > 0 && res.data.data.length === 0) {
                        ita.infoToast('마지막 페이지 입니다.');
                        list.isBusy = false;

                        return;
                    }

                    list.items = res.data.data;
                    list.page = res.data.pageable.page;
                    list.isBusy = false;
                }, function (err) {
                    list.isBusy = false;
                    console.error(err);
                    ita.infoToast('리스트 조회에 실패했습니다.');
                });
        },
        getAppsSort: function (ctx) {
            this.app.list.sort = ctx.sortBy;
            this.app.list.sortDesc = ctx.sortDesc;
            this.app.list.page = 0;
            this.getApps(0);
        },
        getApisSort: function (ctx) {
            this.api.list.sort = ctx.sortBy;
            this.api.list.sortDesc = ctx.sortDesc;
            this.api.list.page = 0;
            this.getApis(0);
        },
        getWebhooksSort: function (ctx) {
            this.webhook.list.sort = ctx.sortBy;
            this.webhook.list.sortDesc = ctx.sortDesc;
            this.webhook.list.page = 0;
            this.getWebhooks(0);
        },
        //스코프 옵션 초기화
        getScopeOption: function () {
            axios.get(CONTEXT_PATH + '/api/v1/scopes/options', [])
                .then(function (res) {
                    if (res.data.code !== 200) {
                        console.error(res);
                        ita.errorToast("스코프 옵션 세팅에 실패했습니다.");
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
                    ita.errorToast("앱 등록에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                    ita.errorToast("앱 등록에 실패했습니다.");
                });
        },//앱 수정
        modifyApp: function () {
            let sUrl = CONTEXT_PATH + '/api/v1/app/' + this.app.manage.values.idx;
            axios.put(sUrl, this.app.manage.values)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.$bvModal.hide("modal-manage-app");
                        ita.app.list.items.splice(ita.app.manage.modifyIdx, 1, res.data.data);
                        ita.infoToast("앱 수정에 성공했습니다.");
                        return;
                    }

                    console.error(res);
                    ita.errorToast("앱 수정에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                    ita.errorToast("앱 수정에 실패했습니다.");
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
                        var addPage = ita.app.list.items.length === 1 ? -1 : 0;
                        ita.getApps(addPage);
                        ita.infoToast('앱 삭제에 성공했습니다.');
                        return;
                    }

                    console.error(res);
                    ita.errorToast("앱 삭제에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                    ita.errorToast("앱 삭제에 실패했습니다.");
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
                        var addPage = ita.webhook.list.items.length === 1 ? -1 : 0;
                        ita.infoToast("웹훅 삭제에 성공했습니다.");
                        ita.getWebhooks(addPage);
                        return;
                    }

                    ita.errorToast("웹훅 삭제에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                    ita.errorToast("웹훅 삭제에 실패했습니다.");
                });
        },
        //웹훅 전체 삭제
        deleteWebhookAll: function () {
            if (confirm('수신한 전체 웹훅을 삭제하시겠습니까?') === false) {
                return;
            }

            let sUrl = CONTEXT_PATH + '/api/v1/webhooks';
            axios.delete(sUrl)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.webhook.list.items = [];
                        ita.webhook.list.page = 0;
                        ita.infoToast("웹훅 전체 삭제에 성했습니다.");
                        return;
                    }
                    console.error(res);
                    ita.errorToast("웹훅 전 삭제에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                    ita.errorToast("웹훅 전체 삭제에 실패했습니다.");
                });
        },
        //api 전체 삭제
        deleteApiAll: function () {
            if (confirm('전체 Api 요청을 삭제하시겠습니까?') === false) {
                return;
            }

            let sUrl = CONTEXT_PATH + '/api/v1/apis';
            axios.delete(sUrl)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.api.list.items = [];
                        ita.api.list.page = 0;
                        ita.infoToast("전체 Api 삭제에 성공했습니다.");

                        return;
                    }

                    console.error(res);
                    ita.errorToast("전체 Api 삭제에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                    ita.errorToast("전체 Api 삭제에 실패했습니다.");
                });
        },//api 단건 삭제
        deleteApi: function (idx) {
            if (confirm(this.api.list.items[idx].idx + '를 삭제하시겠습니까?') === false) {
                return;
            }

            let sUrl = CONTEXT_PATH + '/api/v1/api/' + this.api.list.items[idx].idx;
            axios.delete(sUrl)
                .then(function (res) {
                    if (res.data.code === 200) {
                        var addPage = ita.api.list.items.length === 1 ? -1 : 0;
                        ita.getApis(addPage);
                        ita.infoToast("api 삭제에 성했습니다.");
                        return;
                    }
                    console.error(res);
                    ita.errorToast("api 삭제에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                    ita.errorToast("api 삭제에 실패했습니다.");
                });
        },
        //API 호출
        callApi: function () {
            this.api.manage.apiCalling = true
            axios.post(CONTEXT_PATH + '/api/v1/api', this.api.manage.values)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.api.manage.values = res.data.data;
                        ita.infoToast('api 호출에 성공했습니다.');
                        if (ita.api.list.page === 0 && ita.api.list.sort === 'idx' && ita.api.list.sortDesc === true) {
                            ita.api.list.items.shift();
                            ita.api.list.items.push(res.data.data);
                        } else {
                            ita.api.list.page = 0;
                            ita.api.list.sort = 'idx';
                            ita.api.list.sortDesc = true;
                            ita.getApis(0)
                        }
                        ita.api.manage.apiCalling = false;

                        return;
                    }

                    console.error(res);
                    ita.errorToast("API 호출에 실패했습니다.(" + res.data.message + ")");
                    ita.api.manage.apiCalling = false;
                }, function (err) {
                    ita.api.manage.apiCalling = false;
                    console.error(err);
                    ita.errorToast("API 호출에 실패했습니다.");
                });
        },
        //앱 등록 모달 데이터 초기화
        createAppModalInit: function () {
            this.app.manage.mode = 'register';
            this.app.manage.values = JSON.parse(JSON.stringify(this.app.manage.defaultValues));
        }, //api 호출 모달 데이터 초기화
        callApiModalInit: function () {
            this.api.manage.values = JSON.parse(JSON.stringify(this.api.manage.defaultValues));
            this.setClientIdsApiOption();
        },
        setClientIdsApiOption: function () {
            var clientIds = [];
            this.app.list.items.forEach(function (element) {
                clientIds.push({text: element.clientId + ' :: ' + element.appName, value: element.clientId});
            });

            this.api.manage.options.clientIds = clientIds;
        },
        setMallIdsApiOption: function () {
            if (this.api.manage.values.clientId === '') {
                this.api.manage.options.mallIds = [];
                return false;
            }

            let url = CONTEXT_PATH + '/api/v1/api/' + this.api.manage.values.clientId + '/mallIds';
            axios.get(url)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.api.manage.options.mallIds = res.data.data;

                        return;
                    }

                    console.error(res);
                    ita.errorToast("api 호출이 가능한 몰아이디 조회에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                    ita.errorToast("api 호출이 가능한 몰아이디 조회에 실패했습니다.");
                });
        },
        queryStr: function (params) {
            if (params === undefined) {
                return '';
            }
            return '?' + Object.entries(params).map(e => e.join('=')).join('&');
        },
        substrPath: function () {
            let path = this.api.manage.values.path || '';
            if (path.indexOf('/api/') > 0)
                this.api.manage.values.path = path.substr(path.indexOf('/api/'), path.length)
        },
        regexVersion: function () {
            var regExp = /^\d{4}-\d{2}-\d{2}$/;
            return regExp.test(this.api.manage.values.version);
        },
        searchAll: function () {
            this.app.list.page = 0;
            this.api.list.page = 0;
            this.webhook.list.page = 0;

            this.getApps(0);
            this.getApis(0);
            this.getWebhooks(0);
        },
        copyUrl: function (event) {
            var copyText = document.getElementById('common-input-copy');
            copyText.value = event.target.parentElement.getElementsByClassName('common-url')[0].textContent;
            copyText.style.display = '';
            copyText.select();
            copyText.setSelectionRange(0, 99999);
            document.execCommand("copy");
            copyText.style.display = 'none';
            this.infoToast("url이 복사되었습니다.");
        },
        errorToast:function (msg) {
            console.error(msg);
            this.makeToast('ERROR', msg, 'error');
        },
        infoToast:function (msg) {
            this.makeToast('INFO', msg, 'info');
        },
        makeToast(title, msg, variant) {
            this.$bvToast.toast(msg, {
                title : title,
                autoHideDelay: 3000,
                variant: variant,
                appendToast: true
            })
        }
    },
    created() {
        var searchValue = document.getElementsByClassName('inp-search-client-id');
        if (searchValue != null) {
            this.searchForm.clientId = searchValue[0].dataset.client_id;
        }

        var permisstions = document.getElementById('span-user-operationLevel');
        this.app.manage.options.operationLevel = JSON.parse(permisstions.dataset.options);
        var grantType = document.getElementById('span-user-grantType');
        this.app.manage.options.grantType = JSON.parse(grantType.dataset.options);

        this.getApps(0);
        this.getApis(0);
        this.getWebhooks(0);

        this.getScopeOption();
        this.createAppModalInit();
        this.callApiModalInit();
    },
    mounted() {
    }
})
