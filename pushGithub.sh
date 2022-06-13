echo '开始推送github'
git remote add origin git@github.com:zhangjian11111/crbz-shop.git
echo '设置上传代码分支，推送github'
git push --set-upstream origin master --force
echo '推送github完成'


