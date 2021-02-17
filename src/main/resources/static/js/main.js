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
                ,
                appInfoModal :{
                    title : 'App 등록'
                }
            }
        },
        fields: [
            {key: 'idx', label: 'IDX', sortable: true},
            {key: 'clientId', label: 'client id', sortable: true},
            {key: 'appName', label: '앱 이름', sortable: true},
            {key: 'modifiedDate', label: '수정일시', sortable: true},
            {key: 'actions', label: 'Actions'}
        ],
        items: [
            {isActive: false, idx: 401, clientId: '1Dicke23rson', appName: '4Macdon23ald', modifiedDate: '1Ma3cdonald'},
            {isActive: false, idx: 402, clientId: '2Dick2erson', appName: '3Macdon23ald', modifiedDate: '2Mac23donald'},
            {isActive: false, idx: 403, clientId: '3Dicke32rson', appName: '2Macdona2ld', modifiedDate: '3Ma32cdonald'},
            {isActive: true, idx: 404, clientId: '4Dickers1on', appName: '1Macdona2ld', modifiedDate: '4Macd3onald'}
        ]
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
            this.app.manage.appInfoModal.title= 'APP 수정';
            this.$root.$emit('bv::show::modal', 'modal-manage-app', button);
            this.app.manage.values = item;
            this.app.manage.values.secretKey = '###############';
        },
        //App 리스트 조회
        getApps: function () {
            axios.get('/api/v1/apps' + this.queryStr(ita.searchForm))
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
        },
        //스코프 옵션 초기화
        getScopeOption: function () {
            axios.get('/api/v1/scopes/options', [])
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
        saveApp: function () {
            axios.post('/api/v1/app', this.app.manage.values)
                .then(function (res) {
                    if (res.data.code === 200) {
                        ita.getApps();
                        return;
                    }

                    console.error(res);
                    alert("앱 등록에 실패했습니다.(" + res.data.message + ")");
                }, function (err) {
                    console.error(err);
                });
        },
        //앱 등록 모달 데이터 초기화
        createModalInit: function () {
            this.app.manage.appInfoModal.title= 'APP 등록';
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
        this.getScopeOption();
        this.createModalInit();
        this.getApps();
    },
    mounted() {
    }
})
