./svn-to-git.sh
git fetch pims-repo
git checkout master
git merge -Xtheirs pims-repo/master
git checkout current
git rebase master
# or git merge --squash master
mv ignore-logic .gitignore
git svn dcommit
mv .gitignore ignore-logic
