package com.qcadoo.plugin.internal.dao;

import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.qcadoo.plugin.api.PersistentPlugin;
import com.qcadoo.plugin.internal.DefaultPersistentPlugin;
import com.qcadoo.plugin.internal.api.PluginDao;

@Service
public class DefaultPluginDao implements PluginDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public void save(final PersistentPlugin plugin) {
        sessionFactory.getCurrentSession().save(DefaultPersistentPlugin.class.getCanonicalName(), plugin);
    }

    @Override
    @Transactional
    public void delete(final PersistentPlugin plugin) {
        sessionFactory.getCurrentSession().delete(DefaultPersistentPlugin.class.getCanonicalName(), plugin);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Set<PersistentPlugin> list() {
        return Sets.newHashSet(sessionFactory.getCurrentSession().createCriteria(DefaultPersistentPlugin.class).list());
    }

    void setSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
